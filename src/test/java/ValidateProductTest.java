import DTO.Request.Login.LoginRequest;
import DTO.Request.Product.ProductRequest;
import DTO.Response.Login.LoginResponse;
import DTO.Response.Product.ProductCreateResponse;
import DTO.Response.Product.ProductUpdateResponse;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import static Reports.ExtentTestManager.startTest;
import static io.restassured.RestAssured.given;

//@Listeners(TestListener.class)
public class ValidateProductTest extends ValidateBaseTest {
    @Override
    protected String getEndpoint() {
        return ENDPOINT + "produtos/";
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH + "produtos/";
    }

    @Test(priority = 1)
    public void validateRegistration() {
        startTest("Registration", "validate product registration");

        ProductRequest request = ProductRequest
                .builder()
                .nome("Computador")
                .preco(250)
                .descricao("Do bom")
                .quantidade(1)
                .build();

        ProductCreateResponse response = given()
                .header("authorization", this.loginAndGetAuthorizationToken())
                .header("content-type", "application/json")
                .header("accept-type", "application/json")
                .body(request)
                .when()
                .log()
                .all()
                .post(this.getEndpoint())
                .then()
                .log()
                .all()
                .statusCode(201)
                .extract()
                .body()
                .as(ProductCreateResponse.class);

        Assert.assertEquals(response.getMessage(), "Cadastro realizado com sucesso");
        Assert.assertNotNull(response.getId());

        this.id = response.getId();
    }

    @Test(priority = 2)
    public void validateSearchById() {
        startTest("validateSearchById", "validate search product by id");

        given()
                .header("content-type", "application/json")
                .header("accept-type", "application/json")
                .pathParams("id", this.id)
                .when()
                .log()
                .all()
                .get(this.getEndpoint() + "{id}")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(
                        new File(getFilePath() + "GetOneProductSchema.json")
                ))
                .log()
                .all();
    }

    @Test
    public void validateGeneralSearch() {
        startTest("validateGeneralSearch", "validate product general search");

        given()
                .when()
                .log()
                .all()
                .get(getEndpoint())
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(
                        new File(getFilePath() + "GetAllProductsSchema.json")
                ))
                .log()
                .all();
    }

    @Test(priority = 3)
    public void validateUpdate() {
        startTest("validateUpdate", "validate product update");

        ProductRequest request = ProductRequest
                .builder()
                .nome("Teste")
                .preco(20)
                .descricao("Muito")
                .quantidade(2)
                .build();

        ProductUpdateResponse response = given()
                .header("authorization", this.loginAndGetAuthorizationToken())
                .header("content-type", "application/json")
                .header("accept-type", "application/json")
                .pathParams("id", this.id)
                .body(request)
                .log()
                .all()
                .when()
                .put(this.getEndpoint() + "{id}")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .body()
                .as(ProductUpdateResponse.class);

        Assert.assertEquals(response.getMessage(), "Registro alterado com sucesso");
    }

    @Test(priority = 4)
    public void validateDeletion() {
        startTest("validateDeletion", "validate product deletion");

        given()
                .header("authorization", this.loginAndGetAuthorizationToken())
                .pathParams("id", this.id)
                .when()
                .log()
                .all()
                .delete(this.getEndpoint() + "{id}")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    private String loginAndGetAuthorizationToken() {
        LoginRequest request = LoginRequest.builder()
                .email("fulano@qa.com")
                .password("teste")
                .build();

        LoginResponse response = given()
                .header("accept-type", "application/json")
                .header("content-type", "application/json")
                .body(request)
                .log()
                .all()
                .when()
                .post(ENDPOINT + "login")
                .then()
                .log()
                .all()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .as(LoginResponse.class);

        return response.getAuthorization();
    }
}
