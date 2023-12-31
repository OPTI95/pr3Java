    package com.example.demo.controllers;
    import com.example.demo.model.Customer;
    import com.example.demo.repositories.CustomerRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;


    @Controller
    @RequestMapping("/customers")
    public class CustomerController {
        private final CustomerRepository customerRepository;
        public CustomerController(CustomerRepository customerRepository) {
            this.customerRepository = customerRepository;
        }

        @GetMapping
        public String showAllCustomers(Model model) {
            Iterable<Customer> customers = customerRepository.findAll();
            model.addAttribute("customers", customers);
            return "customer/customerList";
        }

        @GetMapping("/{id}")
        public String showCustomerDetails(@PathVariable Long id, Model model) {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
            model.addAttribute("customer", customer);
            return "customer/customerDetails";
        }

        @GetMapping("/new")
        public String showCustomerForm(Model model) {
            model.addAttribute("customer", new Customer());
            return "customer/customerForm";
        }

        @PostMapping
        public String createCustomer(@ModelAttribute Customer customer, BindingResult result, Model model) {
            if (result.hasErrors()) {
                return "customer/customerForm";
            }

            customerRepository.save(customer);
            model.addAttribute("customers", customerRepository.findAll());

            return "customer/customerList";
        }

        @GetMapping("/{id}/edit")
        public String showEditForm(@PathVariable Long id, Model model) {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
            model.addAttribute("customer", customer);
            return "customer/editCustomerForm";
        }

        @PostMapping("/{id}/edit")
        public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer, BindingResult result, Model model) {
            if (result.hasErrors()) {
                customer.setId(id);
                return "customer/editCustomerForm";
            }

            customerRepository.save(customer);
            model.addAttribute("customers", customerRepository.findAll());
            return "customer/customerList";
        }

        @GetMapping("/{id}/delete")
        public String deleteCustomer(@PathVariable Long id, Model model) {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
            customerRepository.delete(customer);
            model.addAttribute("customers", customerRepository.findAll());
            return "customer/customerList";
        }
    }
