package sia.tacocloud;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

//import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.data.OrderRepository;

//@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
	private OrderRepository orderRepo;

	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "/orderForm";
	}

	@PostMapping
	public String processOrder(@Valid @ModelAttribute("order") Order order, Errors errors, Model model,
			SessionStatus sessionStatus) {
		if (errors.hasErrors()) {
			return "orderForm";
		}
		// log.info("Order submitted: " + order);
		orderRepo.save(order);
		sessionStatus.setComplete();
		return "redirect:/";
	}
}