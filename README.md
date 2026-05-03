# Briefly AI 🤖📄
> **AI-powered document summarizer** — Upload any PDF, DOCX, or TXT and get a clear, simple summary in seconds.

🌐 **Live Site:** [pranathikukutla.github.io/Briefly-AI](https://pranathikukutla.github.io/Briefly-AI/)

---

## What it does

Briefly AI is a study assistant that takes long, dense documents and turns them into clean, readable summaries using AI. Built for students who don't have time to read 100-page PDFs.

Upload a file → AI reads it → You get the key points. That's it.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2 |
| Document Parsing | Apache Tika (PDF, DOCX, TXT) |
| AI Integration | REST API via Spring WebFlux WebClient |
| Database | MongoDB |
| Frontend | HTML, CSS, JavaScript |
| Deployed | GitHub Pages (landing page) |
| Lead Capture | Tally (waitlist form → real-time submissions) |

---

## Features

- 📤 **Upload any format** — PDF, DOCX, or plain text, no reformatting needed
- ⚡ **Instant AI summaries** — key ideas extracted in under 30 seconds
- 🧠 **Deep understanding** — identifies arguments, conclusions, and key points
- 🔒 **Private by default** — documents are never stored or shared
- 🎓 **Built for students** — optimized for textbooks, papers, and lecture notes
- 📱 **Clean UI** — upload, summarize, done

---

## How it works

```
User uploads file (PDF / DOCX / TXT)
        ↓
Apache Tika extracts raw text
        ↓
Spring Boot sends text to AI API via WebFlux
        ↓
AI returns a clean summary
        ↓
Summary displayed to user + stored in MongoDB
```

---

## Local Setup

### Prerequisites
- Java 17+
- Maven
- MongoDB running locally

### Run it

```bash
git clone https://github.com/pranathikukutla/Briefly-AI.git
cd Briefly-AI
./mvnw spring-boot:run
```

App runs at `http://localhost:8080`

---

## Lead Generation

The live landing page includes a **Tally waitlist form** that captures early user emails in real time — demonstrating a full lead capture → CRM pipeline without any backend code.

---

## Project Structure

```
Briefly-AI/
├── src/
│   └── main/
│       ├── java/          # Spring Boot controllers, services, models
│       └── resources/
│           ├── templates/ # HTML frontend
│           └── static/    # CSS, JS assets
├── uploads/               # Temporary file storage
├── docs/                  # GitHub Pages (landing page)
├── pom.xml                # Maven dependencies
└── README.md
```

---

## Built by

**Pranatha Kukutla** — [LinkedIn](#) · [GitHub](https://github.com/pranathikukutla)

> Built as a full-stack AI project combining document processing, LLM integration, and conversion-focused frontend design.