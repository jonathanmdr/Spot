.PHONY: clean install test up down restart upgrade_otel_agent

clean:
	@gradle clean

install:
	@gradle clean build

test:
	@gradle clean test

up:
	@docker-compose up -d

down:
	@docker-compose down --remove-orphans --volumes

restart:
	@docker-compose restart

upgrade_otel_agent:
	@echo "Update local version for OTEL Java Agent..."
	@mkdir -p agents
	@curl -o agents/opentelemetry-javaagent.jar -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
	@curl -sL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases | grep -oE 'releases/tag/v[0-9]+\.[0-9]+\.[0-9]+' | cut -d'/' -f3 | sort -V | tail -n 1 > agents/version.txt
	@VERSION=$$(cat agents/version.txt) && echo "OTEL Java Agent local was updated to $$VERSION"
	@rm -rf agents/version.txt