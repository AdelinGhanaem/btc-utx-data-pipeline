
package btc.da.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


/**
 * The Items Schema
 * <p>
 * 
 * 
 */
public class Out implements Serializable
{

    /**
     * The Spent Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("spent")
    @Expose
    @NotNull
    private Boolean spent = false;
    /**
     * The Tx_index Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("tx_index")
    @Expose
    @NotNull
    private Long txIndex = 0L;
    /**
     * The Type Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("type")
    @Expose
    @NotNull
    private Long type = 0L;
    /**
     * The Addr Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("addr")
    @Expose
    @Pattern(regexp = "^(.*)$")
    @NotNull
    private String addr = "";
    /**
     * The Value Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("value")
    @Expose
    @NotNull
    private Long value = 0L;
    /**
     * The N Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("n")
    @Expose
    @NotNull
    private Long n = 0L;
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
    private final static long serialVersionUID = -766299017444964536L;

    /**
     * The Spent Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Boolean getSpent() {
        return spent;
    }

    /**
     * The Spent Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setSpent(Boolean spent) {
        this.spent = spent;
    }

    /**
     * The Tx_index Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Long getTxIndex() {
        return txIndex;
    }

    /**
     * The Tx_index Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setTxIndex(Long txIndex) {
        this.txIndex = txIndex;
    }

    /**
     * The Type Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Long getType() {
        return type;
    }

    /**
     * The Type Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setType(Long type) {
        this.type = type;
    }

    /**
     * The Addr Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public String getAddr() {
        return addr;
    }

    /**
     * The Addr Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * The Value Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Long getValue() {
        return value;
    }

    /**
     * The Value Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setValue(Long value) {
        this.value = value;
    }

    /**
     * The N Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Long getN() {
        return n;
    }

    /**
     * The N Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setN(Long n) {
        this.n = n;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("spent", spent).append("txIndex", txIndex).append("type", type).append("addr", addr).append("value", value).append("n", n).append("script", script).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(n).append(value).append(addr).append(txIndex).append(script).append(type).append(spent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Out) == false) {
            return false;
        }
        Out rhs = ((Out) other);
        return new EqualsBuilder().append(n, rhs.n).append(value, rhs.value).append(addr, rhs.addr).append(txIndex, rhs.txIndex).append(script, rhs.script).append(type, rhs.type).append(spent, rhs.spent).isEquals();
    }

}
