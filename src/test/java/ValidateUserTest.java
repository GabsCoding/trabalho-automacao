import DTO.Request.Login.LoginRequest;
import DTO.Request.Product.ProductRequest;
import DTO.Request.User.UserRequest;
import DTO.Response.Login.LoginResponse;
import DTO.Response.Product.ProductCreateResponse;
import DTO.Response.Product.ProductUpdateResponse;
import DTO.Response.User.UserCreateResponse;
import DTO.Response.User.UserUpdateResponse;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import static Reports.ExtentTestManager.startTest;
import static io.restassured.RestAssured.given;

//@Listeners(TestListener.class)
public class ValidateUserTest extends ValidateBaseTest {
    @Override
    protected String getEndpoint() {
        return ENDPOINT + "usuarios/";
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH + "usuarios/";
    }

    @Test(priority = 1)
    public void validateRegistration() {
        startTest("Registration", "validate user registration");

        UserRequest request = UserRequest
                .builder()
                .nome("Gabriel")
                .email("gabriel@gmail.com")
                .password("senha")
                .administrador("true")
                .build();

        UserCreateResponse response = given()
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
                .as(UserCreateResponse.class);

        Assert.assertEquals(response.getMessage(), "Cadastro realizado com sucesso");
        Assert.assertNotNull(response.getId());

        this.id = response.getId();
    }

    @Test(priority = 2)
    public void validateSearchById() {
        startTest("validateSearchById", "validate search user by id");

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
                        new File(getFilePath() + "GetOneUserSchema.json")
                ))
                .log()
                .all();
    }

    @Test
    public void validateGeneralSearch() {
        startTest("validateGeneralSearch", "validate user general search");

        given()
                .when()
                .log()
                .all()
                .get(this.getEndpoint())
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(
                        new File(getFilePath() + "GetAllUsersSchema.json")
                ))
                .log()
                .all();
    }

    @Test(priority = 3)
    public void validateUpdate() {
        startTest("validateUpdate", "validate user update");

        UserRequest request = UserRequest
                .builder()
                .nome("Jana")
                .email("jana@gmail.com")
                .password("outra senha")
                .administrador("false")
                .build();

        UserUpdateResponse response = given()
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
                .as(UserUpdateResponse.class);

        Assert.assertEquals(response.getMessage(), "Registro alterado com sucesso");
    }

    @Test(priority = 4)
    public void validateDeletion() {
        startTest("validateDeletion", "validate user deletion");

        given()
                .pathParams("id", this.id)
                .when()
                .log()
                .all()
                .delete(getEndpoint() + "{id}")
                .then()
                .log()
                .all()
                .statusCode(200);
    }
}
