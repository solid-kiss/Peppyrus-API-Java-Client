package be.solid_kiss.peppyrus_api.model;

import java.util.List;

public class Participant{
	private String participantId;
	private String scheme;
	private List<Service> services;

	public void setParticipantId(String participantId){
		this.participantId = participantId;
	}

	public String getParticipantId(){
		return participantId;
	}

	public void setScheme(String scheme){
		this.scheme = scheme;
	}

	public String getScheme(){
		return scheme;
	}

	public void setServices(List<Service> services){
		this.services = services;
	}

	public List<Service> getServices(){
		return services;
	}


	public static class Service {

		private String transportProfile;
		private String process;
		private String endpoint;
		private String documentType;
		private String contact;
		private String description;

		public void setTransportProfile(String transportProfile) {
			this.transportProfile = transportProfile;
		}

		public String getTransportProfile() {
			return transportProfile;
		}

		public void setProcess(String process) {
			this.process = process;
		}

		public String getProcess() {
			return process;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public void setDocumentType(String documentType) {
			this.documentType = documentType;
		}

		public String getDocumentType() {
			return documentType;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getContact() {
			return contact;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
}
