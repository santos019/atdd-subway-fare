package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.utils.database.DataLoader;
import nextstep.utils.database.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.execute();
            dataLoader.loadData();
        });
    }
}