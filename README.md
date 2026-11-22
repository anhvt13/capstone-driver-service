## Project Architecture Overview
### CI/CD Pipeline for ECS Deployment
***
#### Solution - GitHub Actions as CI/CD orchestration
- Using GitHub as a source code repository and GitHub Actions as CI/CD orchestration for applications deployed on Amazon ECS
![ECS CI/CD pipeline Diagram](images/Capstone-CI-CD-Pipelines.jpg)

#### Solution - ECS Blue/Green Deployment Strategy
![ECS Blue/Green Strategy Diagram](images/Capstone-Blue-Green-Deployment.jpg)

- Decision made: 
   - The application deployment pipeline uses Amazon ECS Blue/Green Deployment to release new application versions while minimizing service disruption and reducing the risk when deploying changes directly to the production environment  
   - Blue/Green deployment maintains two application environments:
      - Blue Environment: The currently running production version that serves live traffic
      - Green Environment: The newly deployed application version that is provisioned and validated before receiving production traffic
- Alternative considered: Rolling deployment
   - TBD
- Advantages:
   - Reduced Deployment Downtime: The existing production environment continues serving requests while the new version is deployed and validated
   - Pre-Production Validation: The Green environment can be tested independently before receiving production traffic, allowing deployment health and application behavior to be verified
   - Rollback Capability: If the new version fails health checks or exhibits unexpected behavior, traffic can be redirected to the previous Blue environment, reducing recovery time
   - Deployment Isolation: The new application revision runs separately from the currently active production revision during deployment validation, reducing the risk of introducing an unverified version directly into production
- Disadvantages:
   - Higher Temporary Infrastructure Cost: Both Blue and Green environments may run simultaneously during deployment, increasing ECS task resource consumption and potentially associated infrastructure costs
   - Deployment Complexity: Requires additional configuration for deployment strategies, load balancer target groups, health checks, traffic routing, deployment lifecycle hook, and rollback handling
- Trade-off:
   - Safer release process, the ability to validate a new application revision before production traffic is switched, and a more controlled rollback mechanism
   - Introduces additional infrastructure consumption and deployment orchestration complexity compared with Rolling deployment

#### Technical Challenges
1. Driver-service
- By leverages native ECS Service Connect, service was placed in private network with NO dedicating target groups / load balancer
- With lambda lifecycle hook on green revision validation, default routes test traffic by "x-amzn-ecs-blue-green-test" header expecting HTTP protocol which is NOT compatible with service was designed HTTPs / mTLS protocol. [Document references](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/service-connect-blue-green.html)
- Solution: 
   - Built-in blue/green deployments with Service Connect strategy
   - Lambda lifecycle hook performs health check directly on the green revision by detected task's private IP and served the same HTTPS / mTLS procedure at transport layer
2. BFF-service
- Located in target groups with distributed traffic by the load balancer. [Document References](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/alb-resources-for-blue-green.html)
- Solution:
   - Built-in blue/green deployment with load balancer strategy
   - Route test traffic to the green target group through ALB integration
