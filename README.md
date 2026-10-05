# Coupon API

API REST em Java/Spring Boot para criar, consultar e deletar (soft delete) cupons de desconto.

## Documentação da API (Swagger)

Com a aplicação em execução (local ou via Docker):

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI (JSON): http://localhost:8080/v3/api-docs

## Como rodar

- Local: `./mvnw spring-boot:run`
- Docker: `docker compose up --build` (API em http://localhost:8080); para parar: `docker compose down`
- Testes e cobertura: `./mvnw clean verify` (relatório em `target/site/jacoco/index.html`)

## Decisões de projeto

- Código com mais ou menos de 6 caracteres após sanitização: rejeitado.
- Caixa do código: preservada como enviada.
- Deletar um cupom já deletado retorna `409 Conflict`.
- Imagem Docker: multi-stage (JDK 21 para build, JRE 21 para execução, usuário não-root). O build da imagem pula os testes (`-DskipTests`); o gate oficial é `./mvnw clean verify`.
- Banco H2 em memória: os dados não persistem entre reinícios do container.

## Exemplo de uso (curl)

```bash
# Criar (código sanitizado para ABC123)
curl -X POST http://localhost:8080/coupon -H 'Content-Type: application/json'   -d '{"code":"AB-C12!3","description":"Cupom de teste","discountValue":10.5,"expirationDate":"2030-12-31T23:59:59Z","published":true}'

# Buscar
curl http://localhost:8080/coupon/{id}

# Deletar (soft delete, 204)
curl -X DELETE http://localhost:8080/coupon/{id}
```
