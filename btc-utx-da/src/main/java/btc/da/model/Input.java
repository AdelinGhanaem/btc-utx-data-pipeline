
package btc.da.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


/**
 * The Items Schema
 * <p>
 * 
 * 
 */
public class Input implements Serializable
{

    /**
     * The Sequence Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("sequence")
    @Expose
    @NotNull
    private Long sequence = 0L;
    /**
     * The Prev_out Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("prev_out")
    @Expose
    @Valid
    @NotNull
    private PrevOut prevOut;
    /**
     * The Script Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("script")
    @Expose
    @Pattern(regexp = "^(.*)$")
    @NotNull
    private String script = "";
    private final static long serialVersionUID = -2464830850000063776L;

    /**
     * The Sequence Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Long getSequence() {
        return sequence;
    }

    /**
     * The Sequence Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * The Prev_out Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public PrevOut getPrevOut() {
        return prevOut;
    }

    /**
     * The Prev_out Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setPrevOut(PrevOut prevOut) {
        this.prevOut = prevOut;
    }

    /**
     * The Script Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public String getScript() {
        return script;
    }

    /**
     * The Script Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setScript(String script) {
        this.script = script;
    }

}
