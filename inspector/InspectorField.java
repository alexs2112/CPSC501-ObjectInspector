package inspector;

public class InspectorField {
    public String modifiers;
    public String type;
    public String name;
    public String value;

    public InspectorField(String modifiers, String type, String name, String value) {
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
