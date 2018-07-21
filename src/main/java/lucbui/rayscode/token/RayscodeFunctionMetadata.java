package lucbui.rayscode.token;

/**
 * A package which encompasses a RayscodeFunction and it's metadata.
 */
public class RayscodeFunctionMetadata {

    private String id;
    private RayscodeFunction function;

    /**
     * Create a RayscodeFunction with metadata.
     * @param id Some sort of identifier.
     * @param function A function to use.
     */
    private RayscodeFunctionMetadata(String id, RayscodeFunction function) {
        this.id = id;
        this.function = function;
    }

    /**
     * Create a RayscodeFunction with metadata. id is automatically null.
     * @param function A function to use.
     */
    public static RayscodeFunctionMetadata make(RayscodeFunction function){
        if(function.requiresId()){
            throw new IllegalArgumentException(function + " needs a non-null ID!");
        }
        return new RayscodeFunctionMetadata(null, function);
    }

    /**
     * Create a RayscodeFunction with metadata.
     * @param id Some sort of identifier.
     * @param function A function to use.
     */
    public static RayscodeFunctionMetadata make(String id, RayscodeFunction function){
        if(function.requiresId() && id == null){
            throw new IllegalArgumentException(function + " needs a non-null ID!");
        }
        return new RayscodeFunctionMetadata(id, function);
    }

    /**
     * Get the identifier of this code.
     * @return The identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * The function this metadata performs.
     * @return The function performed.
     */
    public RayscodeFunction getFunction() {
        return function;
    }

    @Override
    public String toString(){
        if(id == null){
            return function.toString();
        } else {
            return function.toString() + ":" + id;
        }
    }
}
