abstract public class ValidateBaseTest {
    protected String id;

    protected static final String ENDPOINT = "https://serverest.dev/";

    protected static final String FILE_PATH = "src/test/resources/";

    protected abstract String getEndpoint();

    protected abstract String getFilePath();
}
