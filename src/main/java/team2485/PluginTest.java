package team2485;

import java.util.List;


import com.google.common.collect.ImmutableList;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import team2485.widgets.MappedDataWidget;
import team2485.widgets.TestWidget;

@Description(group = "team2485", name = "Team 2485 Test", version = "1.0.0", summary = "Team 2485 Testing Plugin Development")
public class PluginTest extends Plugin {




    @Override
    @SuppressWarnings("rawtypes")
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
            WidgetType.forAnnotatedWidget(TestWidget.class),
            WidgetType.forAnnotatedWidget(MappedDataWidget.class)
        );
    }


}
