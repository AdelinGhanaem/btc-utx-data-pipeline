
package com.btc.transactions.reader.btctransactionsreader.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * The Prev_out Schema
 * <p>
 * 
 * 
 */
public class PrevOut implements Serializable
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
    private Integer txIndex = 0;
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
    private Integer type = 0;
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
    private Integer n = 0;
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
    private final static long serialVersionUID = 2392475838748976681L;

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
    public Integer getTxIndex() {
        return txIndex;
    }

    /**
     * The Tx_index Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setTxIndex(Integer txIndex) {
        this.txIndex = txIndex;
    }

    /**
     * The Type Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getType() {
        return type;
    }

    /**
     * The Type Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setType(Integer type) {
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
    public Integer getN() {
        return n;
    }

    /**
     * The N Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setN(Integer n) {
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
        if ((other instanceof PrevOut) == false) {
            return false;
        }
        PrevOut rhs = ((PrevOut) other);
        return new EqualsBuilder().append(n, rhs.n).append(value, rhs.value).append(addr, rhs.addr).append(txIndex, rhs.txIndex).append(script, rhs.script).append(type, rhs.type).append(spent, rhs.spent).isEquals();
    }

}
