package sendman.backend.common.config;

import ai.djl.Model;
import ai.djl.ModelException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;

@Component
public class ModelConfig  {
//    private final Model model;
//
//    public ModelConfig() throws ModelException, IOException {
//        model = Model.newInstance("model");
//        model.load(Paths.get("src/main/resources/models/model.pt"));
//    }
//
//    public Model getModel() {
//        return model;
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        //종료 전 model 저장
//        model.save(Paths.get("src/main/resources/models/model.pt"), null);
//    }
}
