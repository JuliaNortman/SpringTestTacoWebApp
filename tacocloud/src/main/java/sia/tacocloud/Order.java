package sia.tacocloud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.Data;
import sia.tacocloud.entity.User;

@Data
@Entity
@Table(name = "Taco_Order")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	public Order() {
		// user = (User)
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			user = (User) authentication.getPrincipal();
		}
		if (user != null) {
			name = user.getFullname();
			street = user.getStreet();
			city = user.getCity();
			state = user.getState();
			zip = user.getZip();
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "placed_at")
	private Date placedAt;

	@Transient
	// @ManyToOne
	private User user;

	@NotBlank(message = "Delivery name is required")
	private String name;

	@NotBlank(message = "Street is required")
	private String street;

	@NotBlank(message = "City is required")
	private String city;

	@NotBlank(message = "State is required")
	private String state;

	@NotBlank(message = "Zip code is required")
	private String zip;

	@Column(name = "cc_number")
	@CreditCardNumber(message = "Not a valid credit card number")
	private String ccNumber;

	@Column(name = "cc_expiration")
	@Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be formatted MM/YY")
	private String ccExpiration;

	@Column(name = "cc_cvv")
	@Digits(integer = 3, fraction = 0, message = "Invalid CVV")
	private String ccCVV;

	@ManyToMany(targetEntity = Taco.class)
	@JoinTable(joinColumns = { @JoinColumn(name = "tacoOrder") }, inverseJoinColumns = { @JoinColumn(name = "taco") })
	private List<Taco> tacos = new ArrayList<>();

	public void addDesign(Taco design) {
		this.tacos.add(design);
	}

	@PrePersist
	void placedAt() {
		this.placedAt = new Date();
	}

}