package lprtt;
import lombok.Getter;
import org.springframework.core.env.Environment;
public class AppProperties {
    private static AppProperties instance;
    @Getter
    private final Environment environment;
    public AppProperties(Environment env){
        environment = env;
        instance = this;
    }
    public static AppProperties getInstance(){
        if (instance == null){
            throw new RuntimeException("No se inicio AppProperties");
        }
        return instance;
    }

    public Integer defaultPort(){
        return environment.getProperty("server.port", Integer.class);
    }

}
