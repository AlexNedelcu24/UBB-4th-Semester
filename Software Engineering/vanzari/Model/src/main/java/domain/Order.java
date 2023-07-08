package domain;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    @Column(name = "product_ids")
    private String productIds;

    @Column(name = "sales_agent_username")
    private String salesAgentUsername;

    @Column(name = "client_username")
    private String clientUsername;

    @Column(name = "message")
    private String message;

    public Order() {
    }

    public Order(String productIds, String salesAgentUsername, String clientUsername, String message) {
        this.productIds = productIds;
        this.salesAgentUsername = salesAgentUsername;
        this.clientUsername = clientUsername;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getSalesAgentUsername() {
        return salesAgentUsername;
    }

    public void setSalesAgentUsername(String salesAgentUsername) {
        this.salesAgentUsername = salesAgentUsername;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productIds='" + productIds + '\'' +
                ", clientUsername=" + clientUsername +
                ", message=" + message +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(productIds, order.productIds) && Objects.equals(salesAgentUsername, order.salesAgentUsername) && Objects.equals(clientUsername, order.clientUsername) && Objects.equals(message, order.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productIds, salesAgentUsername, clientUsername, message);
    }
}
