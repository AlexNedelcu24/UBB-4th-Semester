package domain;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Offers")
public class Offer implements Serializable {

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

    public Offer() {
    }

    public Offer(String productIds, String salesAgentUsername, String clientID) {
        this.productIds = productIds;
        this.salesAgentUsername = salesAgentUsername;
        this.clientUsername = clientID;
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

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", productIds='" + productIds + '\'' +
                ", salesAgentUsername=" + salesAgentUsername +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(productIds, offer.productIds) && Objects.equals(salesAgentUsername, offer.salesAgentUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productIds, salesAgentUsername);
    }
}

