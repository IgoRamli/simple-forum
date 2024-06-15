# simple-forum
Forum application created as a personal project to learn various aspects of software development, such as frontend, backend, CI/CD, DevOps, and even AI/ML

## How to run locally
1. Run `bin/kc.sh start-dev --http-port=8100` in keycloak directory to start Keycloak service using port 8100
2. Configure Keycloak with the necessary realms, clients, etc. (To be added later)
3. Update client name and secrets in both core and web service
4. Run flyway migration for core service. See README in core directory for more details
5. Run `mvn spring-boot:run` (Or use IntelliJ configuration) in core directory to start Core service
6. Run `npm start dev` in web directory to start Web service
7. You can now access your service by going to http://localhost:3000