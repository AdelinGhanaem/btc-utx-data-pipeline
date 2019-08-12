
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


/**
 * The Root Schema
 * <p>
 */
public class UTX implements Serializable {

    /**
     * The Op Schema
     * <p>
     * <p>
     * (Required)
     */
    @SerializedName("op")
    @Expose
    @Pattern(regexp = "^(.*)$")
    @NotNull
    private String op = "";
    /**
     * The X Schema
     * <p>
     * <p>
     * (Required)
     */
    @SerializedName("x")
    @Expose
    @Valid
    @NotNull
    private X x;
    private final static long serialVersionUID = 7465922853958877139L;

    /**
     * The Op Schema
     * <p>
     * <p>
     * (Required)
     */
    public String getOp() {
        return op;
    }

    /**
     * The Op Schema
     * <p>
     * <p>
     * (Required)
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * The X Schema
     * <p>
     * <p>
     * (Required)
     */
    public X getX() {
        return x;
    }

    /**
     * The X Schema
     * <p>
     * <p>
     * (Required)
     */
    public void setX(X x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("op", op).append("x", x).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(op).append(x).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UTX) == false) {
            return false;
        }
        UTX rhs = ((UTX) other);
        return new EqualsBuilder().append(op, rhs.op).append(x, rhs.x).isEquals();
    }

}
