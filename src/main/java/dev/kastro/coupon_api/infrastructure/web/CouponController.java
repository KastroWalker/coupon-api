package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.application.usecase.CreateCouponUseCase;
import dev.kastro.coupon_api.application.usecase.DeleteCouponUseCase;
import dev.kastro.coupon_api.application.usecase.GetCouponUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Coupons", description = "Criação, consulta e deleção de cupons de desconto")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final GetCouponUseCase getCouponUseCase;

    private final DeleteCouponUseCase deleteCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase, GetCouponUseCase getCouponUseCase,
                            DeleteCouponUseCase deleteCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
        this.getCouponUseCase = getCouponUseCase;
        this.deleteCouponUseCase = deleteCouponUseCase;
    }

    @Operation(summary = "Consultar cupom", description = "Retorna o cupom pelo id.")
    @ApiResponse(responseCode = "200", description = "Cupom encontrado")
    @ApiResponse(responseCode = "400", description = "Id não é um UUID válido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cupom não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/coupon/{id}")
    public CouponResponse get(@PathVariable UUID id) {
        return CouponResponse.from(getCouponUseCase.execute(id));
    }

    @Operation(summary = "Deletar cupom",
            description = "Soft delete: os dados do cadastro são preservados e o status passa a DELETED.")
    @ApiResponse(responseCode = "204", description = "Cupom deletado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Id não é um UUID válido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cupom não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Cupom já deletado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/coupon/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteCouponUseCase.execute(id);
    }

    @Operation(summary = "Criar cupom",
            description = "Cria um cupom. Caracteres especiais do código são removidos antes da validação.")
    @ApiResponse(responseCode = "201", description = "Cupom criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (código, valor de desconto, data de expiração ou corpo malformado)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/coupon")
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@Valid @RequestBody CreateCouponRequest request) {
        return CouponResponse.from(createCouponUseCase.execute(request.toCommand()));
    }
}
