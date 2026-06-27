package com.jsp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.jsp.entity.DeliveryAgent;
import com.jsp.exception.ResourceAlreadyExistsException;
import com.jsp.exception.ResourceNotFoundException;
import com.jsp.exception.InvalidInputException;
import com.jsp.repository.DeliveryAgentRepository;

@Service
public class DeliveryAgentService {

	@Autowired
	private DeliveryAgentRepository deliveryAgentRepository;

	public DeliveryAgent createAgent(DeliveryAgent agent) {

		if (agent.getContact().length() != 10)
			throw new InvalidInputException("Contact must be 10 digits");

		if (deliveryAgentRepository.existsByContact(agent.getContact()))
			throw new ResourceAlreadyExistsException("Agent already exists with contact: " + agent.getContact());

		if (deliveryAgentRepository.existsByVehicleNumber(agent.getVehicleNumber()))
			throw new ResourceAlreadyExistsException(
					"Agent already exists with vehicle number: " + agent.getVehicleNumber());

		if (agent.getRating() < 1.0 || agent.getRating() > 5.0)
			throw new InvalidInputException("Rating must be between 1 and 5");
		return deliveryAgentRepository.save(agent);
	}

	public List<DeliveryAgent> getAllAgents() {
		List<DeliveryAgent> agents = deliveryAgentRepository.findAll();
		if (agents.isEmpty())
			throw new ResourceNotFoundException("No agents found");
		return agents;
	}

	public DeliveryAgent getById(int id) {
		DeliveryAgent deliveryAgent = deliveryAgentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + id));
		return deliveryAgent;
	}

	public DeliveryAgent getByVehicleNumber(String vehicleNumber) {

		DeliveryAgent deliveryAgent = deliveryAgentRepository.findByVehicleNumber(vehicleNumber).orElseThrow(
				() -> new ResourceNotFoundException("Agent not found with vehicleNumber: " + vehicleNumber));
		return deliveryAgent;
	}

	public DeliveryAgent getByContact(String contact) {

		DeliveryAgent deliveryAgent = deliveryAgentRepository.findByContact(contact)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with  contact: " + contact));
		return deliveryAgent;
	}

	public List<DeliveryAgent> getByRatingGreaterThan(Double rating) {
		if (rating < 1.0 || rating > 5.0)
			throw new InvalidInputException("Rating must be between 1 and 5");
		List<DeliveryAgent> agents = deliveryAgentRepository.findByRatingGreaterThan(rating);
		if (agents.isEmpty())
			throw new ResourceNotFoundException("No agents found with rating greater than: " + rating);
		return agents;
	}

	public List<DeliveryAgent> sortByRatingDescending() {
		List<DeliveryAgent> agents = deliveryAgentRepository.findAll(Sort.by("rating").descending());
		if (agents.isEmpty())
			throw new ResourceNotFoundException("No agents found");
		return agents;
	}

	public DeliveryAgent updateAgent(Integer agentId, DeliveryAgent updatedAgent) {
		DeliveryAgent existing = deliveryAgentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

		if (!existing.getContact().equals(updatedAgent.getContact())
				&& deliveryAgentRepository.existsByContact(updatedAgent.getContact()))
			throw new ResourceAlreadyExistsException("Agent already exists with contact: " + updatedAgent.getContact());

		if (!existing.getVehicleNumber().equals(updatedAgent.getVehicleNumber())
				&& deliveryAgentRepository.existsByVehicleNumber(updatedAgent.getVehicleNumber()))
			throw new ResourceAlreadyExistsException(
					"Agent already exists with vehicle number: " + updatedAgent.getVehicleNumber());

		if (updatedAgent.getContact().length() != 10)
			throw new InvalidInputException("Contact must be 10 digits");

		if (updatedAgent.getRating() < 1.0 || updatedAgent.getRating() > 5.0)
			throw new InvalidInputException("Rating must be between 1 and 5");

		existing.setName(updatedAgent.getName());
		existing.setContact(updatedAgent.getContact());
		existing.setVehicleNumber(updatedAgent.getVehicleNumber());
		existing.setRating(updatedAgent.getRating());
		existing.setAvailabilityStatus(updatedAgent.getAvailabilityStatus());

		return deliveryAgentRepository.save(existing);
	}

	public DeliveryAgent updateAgentAvailability(Integer agentId, Boolean status) {
		DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

		if (agent.getAvailabilityStatus().equals(status))
			throw new InvalidInputException("Agent availability is already set to: " + status);

		agent.setAvailabilityStatus(status);
		return deliveryAgentRepository.save(agent);
	}

	public String deleteAgent(Integer agentId) {
		DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

		if (!agent.getAvailabilityStatus())
			throw new InvalidInputException("Cannot delete agent who is currently assigned to a shipment");

		deliveryAgentRepository.delete(agent);
		return "Agent deleted successfully";
	}
}