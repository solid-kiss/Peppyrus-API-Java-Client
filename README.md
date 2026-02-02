# Peppyrus API Java Client

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven Central](https://img.shields.io/maven-central/v/be.solid-kiss/peppyrus-api-client.svg)](https://search.maven.org/artifact/be.solid-kiss/peppyrus-api-client)

Java client library for the Peppyrus API - A free and reliable PEPPOL Access Point for electronic document exchange.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Quick Start](#quick-start)
- [Usage Examples](#usage-examples)
- [API Reference](#api-reference)
- [Contributing](#contributing)

## About

Peppyrus is a free PEPPOL Access Point designed to simplify and streamline electronic document exchange using the PEPPOL network. This Java client library provides seamless integration with the Peppyrus API for:

- Sending and receiving electronic invoices
- Managing order documents
- Looking up PEPPOL participants
- Validating UBL-compliant messages

Learn more about Peppyrus at [https://www.peppyrus.be](https://www.peppyrus.be)

## Features

- **Native Java 17 HttpClient** - No external HTTP dependencies required
- **Fluent API** - Builder pattern for intuitive configuration
- **Type-safe** - Strong typing with comprehensive exception handling
- **Multiple Environments** - Support for TEST and PRODUCTION environments
- **JSON Serialization** - Automatic parsing with Jackson
- **Thread-safe** - Built on modern Java HttpClient
- **Comprehensive Testing** - Extensive unit and integration test coverage
- **Minimal Dependencies** - Only Jackson for JSON processing

[//]: # (## Installation)

[//]: # ()
[//]: # (### Maven)

[//]: # ()
[//]: # (Add the following dependency to your `pom.xml`:)

[//]: # ()
[//]: # (```xml)

[//]: # (<dependency>)

[//]: # (    <groupId>io.github.solid_kiss</groupId>)

[//]: # (    <artifactId>peppyrus-api-client</artifactId>)

[//]: # (    <version>1.0.0</version>)

[//]: # (</dependency>)

[//]: # (```)

[//]: # (### Gradle)

[//]: # ()
[//]: # (Add the following to your `build.gradle`:)

[//]: # ()
[//]: # (```gradle)

[//]: # (implementation 'be.solid-kiss:peppyrus-api-client:1.0.0')

[//]: # (```)

### Requirements

- Java 17 or higher
- Maven 3.6+ (for building from source)

## Quick Start

```java
import io.github.solid_kiss.peppyrus_api.client.PeppyrusClient;
import io.github.solid_kiss.peppyrus_api.client.PeppyrusClientConfig;
import io.github.solid_kiss.peppyrus_api.model.*;
import io.github.solid_kiss.peppyrus_api.client.PeppyrusEnv;


// Create client for TEST environment
PeppyrusClient client = PeppyrusClient.create(
    "your-api-key", 
    PeppyrusEnv.TEST
);

// Get organization information
OrganizationInfo info = client.organization().getInfo();
System.out.println("Organization: " + info.getName());

// List messages
MessageList messages = client.messages().listMessages();
System.out.println("Total messages: " + messages.getMeta().itemCount());

// Lookup a PEPPOL participant
Participant participant = client.peppol()
    .getBestMatch("0123456789", "BE");
System.out.println("Participant ID: " + participant.getParticipantId());
```

### Environments

| Environment | Base URL |
|------------|----------|
| TEST | `https://api.test.peppyrus.be/v1` |
| PRODUCTION | `https://api.peppyrus.be/v1` |

## Usage Examples

### Sending a Message

```java
// Prepare message body
MessageBody messageBody = new MessageBody();
messageBody.setSender("9925:be0123456789");
messageBody.setRecipient("9925:be9876543210");

// Encode XML content to base64
String xmlContent = "<?xml version=\"1.0\"?>..."; // Your UBL XML
String base64Content = Base64.getEncoder().encodeToString(xmlContent.getBytes());
messageBody.setFileContent(base64Content);

// Send message
Message sentMessage = client.messages().postMessage(messageBody);
System.out.println("Message sent with ID: " + sentMessage.getId());
```

### Listing Messages with Filters

```java
import io.github.solid_kiss.peppyrus_api.client.services.PeppyrusMessageClient.MessageListParams;

// List unconfirmed messages in inbox
MessageList messages = client.messages().listMessages(
    new MessageListParams()
        .folder("INBOX")
        .confirmed(false)
        .page(1)
        .perPage(25)
);

for (Message msg : messages.getItems()) {
    System.out.println("From: " + msg.getSender() + 
                      " - Created: " + msg.getCreated());
}
```

### Retrieving and Confirming a Message

```java
String messageId = "012345678-9abc-def0-123456789abc";

// Get message details
Message message = client.messages().getMessage(messageId);
System.out.println("Sender: " + message.getSender());
System.out.println("Recipient: " + message.getRecipient());

// Confirm message reception
if (!message.getConfirmed()) {
    Boolean confirmed = client.messages().confirmMessage(messageId);
    System.out.println("Message confirmed: " + confirmed);
}

// Get validation report
MessageReport report = client.messages().getMessageReport(messageId);
System.out.println("Validation rules: " + report.getValidationRules().size());
```

### Looking Up PEPPOL Participants

```java
// Find best match by VAT number and country
Participant participant = client.peppol()
    .getBestMatch("0123456789", "BE");
System.out.println("Participant: " + participant.getParticipantId());

// Direct lookup by participant ID
Participant lookup = client.peppol()
    .lookup("9925:be0123456789");

// List available services
for (Participant.Service service : lookup.getServices()) {
    System.out.println("Service: " + service.getDescription());
    System.out.println("Document Type: " + service.getDocumentType());
}
```

### Searching the PEPPOL Directory

```java
import io.github.solid_kiss.peppyrus_api.client.services.PeppyrusPeppolClient.PeppolSearchParams;

// Simple search
List<BusinessCard> results = client.peppol()
    .search("Company Name");

// Advanced search with multiple criteria
List<BusinessCard> advancedResults = client.peppol().search(
    new PeppolSearchParams()
        .country("BE")
        .name("Tigron")
        .identifierScheme("VAT")
);

for (BusinessCard card : advancedResults) {
    System.out.println("Found: " + card.getParticipant().identifier());
}
```

### Getting Organization Information

```java
// Organization details
OrganizationInfo info = client.organization().getInfo();
System.out.println("Name: " + info.getName());
System.out.println("VAT: " + info.getVAT());
System.out.println("Address: " + info.getStreet() + " " + info.getHouseNumber());
System.out.println("City: " + info.getZipCode() + " " + info.getCity());

// PEPPOL-specific information
OrganizationPeppolInfo peppolInfo = client.organization().getPeppol();
System.out.println("Participant ID: " + peppolInfo.getParticipants().participantId());
System.out.println("Can receive: " + peppolInfo.getParticipants().canReceive());
System.out.println("Can send: " + peppolInfo.getParticipants().isSender());
```

### Error Handling

```java
import io.github.solid_kiss.peppyrus_api.client.PeppyrusApiException;

try {
    Message message = client.messages().getMessage("non-existent-id");
} catch (PeppyrusApiException e) {
    if (e.isAuthenticationError()) {
        System.err.println("Authentication failed. Check your API key.");
    } else if (e.isNotFoundError()) {
        System.err.println("Message not found: " + e.getMessage());
    } else if (e.isValidationError()) {
        System.err.println("Validation error: " + e.getMessage());
    } else {
        System.err.println("API error (" + e.getStatusCode() + "): " + e.getMessage());
    }
}
```

## API Reference

### Main Client

#### `PeppyrusClient`

Entry point for all Peppyrus services.

**Factory Methods:**
- `create(String apiKey, PeppyrusEnv env)` - Create client with API key and environment

**Service Accessors:**
- `messages()` - Returns `PeppyrusMessageClient` for message operations
- `organization()` - Returns `PeppyrusOrganizationClient` for organization operations
- `peppol()` - Returns `PeppyrusPeppolClient` for PEPPOL lookup operations

### Message Operations

#### `PeppyrusMessageClient`

**Methods:**
- `postMessage(MessageBody body)` - Send a new message
- `listMessages()` - List all messages
- `listMessages(MessageListParams params)` - List messages with filters
- `getMessage(String id)` - Retrieve a specific message
- `confirmMessage(String id)` - Confirm message reception
- `deleteMessage(String id)` - Delete a message
- `getMessageReport(String id)` - Get validation report

**MessageListParams:**
- `folder(String)` - Filter by folder (INBOX, OUTBOX, SENT, FAILED)
- `sender(String)` - Filter by sender participant ID
- `receiver(String)` - Filter by receiver participant ID
- `confirmed(Boolean)` - Filter by confirmation status
- `page(Integer)` - Page number
- `perPage(Integer)` - Items per page (max 100)

### Organization Operations

#### `PeppyrusOrganizationClient`

**Methods:**
- `getInfo()` - Retrieve organization information
- `getPeppol()` - Retrieve PEPPOL-specific information

### PEPPOL Operations

#### `PeppyrusPeppolClient`

**Methods:**
- `getBestMatch(String vatNumber, String countryCode)` - Find best participant by VAT
- `lookup(String participantId)` - Lookup participant capabilities in SMP
- `search(String query)` - Simple search in PEPPOL directory
- `search(PeppolSearchParams params)` - Advanced search with multiple criteria

**PeppolSearchParams:**
- `query(String)` - General search query
- `participantId(String)` - Participant ID
- `name(String)` - Organization name
- `country(String)` - Country code
- `geoInfo(String)` - Geographic information
- `contact(String)` - Contact information
- `identifierScheme(String)` - Identifier scheme (e.g., "VAT")
- `identifierValue(String)` - Identifier value

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Related Links

- [Peppyrus Website](https://www.peppyrus.be)
- [Peppyrus Customer Portal (Test)](https://customer.test.peppyrus.be)
- [Peppyrus Customer Portal (Production)](https://customer.peppyrus.be)
- [PEPPOL Network](https://peppol.org)

---

**Note**: This is an unofficial client library. For official support and service, please contact Peppyrus directly.
