
package btc.da.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;


/**
 * The X Schema
 * <p>
 * 
 * 
 */
public class X implements Serializable
{

    /**
     * The Lock_time Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("lock_time")
    @Expose
    @NotNull
    private Integer lockTime = 0;
    /**
     * The Ver Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("ver")
    @Expose
    @NotNull
    private Integer ver = 0;
    /**
     * The Size Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("size")
    @Expose
    @NotNull
    private Integer size = 0;
    /**
     * The Inputs Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("inputs")
    @Expose
    @Valid
    @NotNull
    private List<Input> inputs = null;
    /**
     * The Time Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("time")
    @Expose
    @NotNull
    private Integer time = 0;
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
     * The Vin_sz Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("vin_sz")
    @Expose
    @NotNull
    private Integer vinSz = 0;
    /**
     * The Hash Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("hash")
    @Expose
    @Pattern(regexp = "^(.*)$")
    @NotNull
    private String hash = "";
    /**
     * The Vout_sz Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("vout_sz")
    @Expose
    @NotNull
    private Integer voutSz = 0;
    /**
     * The Relayed_by Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("relayed_by")
    @Expose
    @Pattern(regexp = "^(.*)$")
    @NotNull
    private String relayedBy = "";
    /**
     * The Out Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("out")
    @Expose
    @Valid
    @NotNull
    private List<Out> out = null;
    private final static long serialVersionUID = 7971847439509590254L;

    /**
     * The Lock_time Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getLockTime() {
        return lockTime;
    }

    /**
     * The Lock_time Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setLockTime(Integer lockTime) {
        this.lockTime = lockTime;
    }

    /**
     * The Ver Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getVer() {
        return ver;
    }

    /**
     * The Ver Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setVer(Integer ver) {
        this.ver = ver;
    }

    /**
     * The Size Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getSize() {
        return size;
    }

    /**
     * The Size Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * The Inputs Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public List<Input> getInputs() {
        return inputs;
    }

    /**
     * The Inputs Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    /**
     * The Time Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getTime() {
        return time;
    }

    /**
     * The Time Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setTime(Integer time) {
        this.time = time;
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
     * The Vin_sz Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getVinSz() {
        return vinSz;
    }

    /**
     * The Vin_sz Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setVinSz(Integer vinSz) {
        this.vinSz = vinSz;
    }

    /**
     * The Hash Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public String getHash() {
        return hash;
    }

    /**
     * The Hash Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * The Vout_sz Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public Integer getVoutSz() {
        return voutSz;
    }

    /**
     * The Vout_sz Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setVoutSz(Integer voutSz) {
        this.voutSz = voutSz;
    }

    /**
     * The Relayed_by Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public String getRelayedBy() {
        return relayedBy;
    }

    /**
     * The Relayed_by Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    /**
     * The Out Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public List<Out> getOut() {
        return out;
    }

    /**
     * The Out Schema
     * <p>
     * 
     * (Required)
     * 
     */
    public void setOut(List<Out> out) {
        this.out = out;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lockTime", lockTime).append("ver", ver).append("size", size).append("inputs", inputs).append("time", time).append("txIndex", txIndex).append("vinSz", vinSz).append("hash", hash).append("voutSz", voutSz).append("relayedBy", relayedBy).append("out", out).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(voutSz).append(time).append(inputs).append(lockTime).append(hash).append(relayedBy).append(vinSz).append(txIndex).append(ver).append(out).append(size).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof X) == false) {
            return false;
        }
        X rhs = ((X) other);
        return new EqualsBuilder().append(voutSz, rhs.voutSz).append(time, rhs.time).append(inputs, rhs.inputs).append(lockTime, rhs.lockTime).append(hash, rhs.hash).append(relayedBy, rhs.relayedBy).append(vinSz, rhs.vinSz).append(txIndex, rhs.txIndex).append(ver, rhs.ver).append(out, rhs.out).append(size, rhs.size).isEquals();
    }

}
