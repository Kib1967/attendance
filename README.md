# Attendance application

## Introduction

This application is a replacement for the paper-based attendance record at the Oxford office of an unnamed large German industrial company. I started it as a project to expand my knowledge of Spring and some web technologies. Development is therefore driven by which parts of the application I consider "interesting", and nothing else.

## Development

### Prerequisites

All you should need to get started with Attendance is a JDK, a git client (I'm using the one built in to Eclipse, but it's entirely up to you). If you want to contribute, you'll need a github account, and commit access to my repository.

By the magic of Gradle, you don't need anything else (not even Gradle itself).

### Technologies used

* Gradle: of course
* Spring: various parts of the Spring framework are used, notably spring-data to abstract the database layer
* Spring Boot: used to get appropriate default configuration of the huge number of spring beans involved, otherwise just getting a simple app running is horrific for a Spring newbie
* Hibernate: to underpin spring-data (you shouldn't need to care about it)
* h2: as an in-memory, non-persistent database for development and automated testing
* Postgresql: as an alternative when deployed in the cloud (see below)
* Thymeleaf: as a template engine, to create HTML pages
* bootstrap: as a theme
* jquery: underpinning bootstrap
* font-awesome: for icons
* A servlet container: in practice you don't need to care which, since Spring includes and configures one for you

## Requirements

I don't have any desire to start writing requirements at this point in my career, but if more than one person is going to work on it... so I'll use single-line descriptions of user needs:

* security - some mechanism for authenticating users. At this stage, in development, just an in-memory authentication method. In a real example, we'd use whatever already existed, almost certainly based on LDAP
* create a new "attendance event" (e.g. a leave request)
* list existing events
* some event types require manager approval
* managers are notified by email when a new item is created for someone in their team
* managers can approve or deny requests, and can add a narrative on requests that they deny
* missing functionality can be swept up in an all-purpose "admin" function that more or less allows access to the underlying database
* all actions performed by admin users are added to an audit trail
* the audit trail can be viewed by admins
* calendars can be defined that cover e.g. UK bank holidays, office closures. Users adopt a particular calendar or calendars
* admin: can create users
* admin: can delete users
* admin: can set user holiday entitlement
* user: can change password (perhaps... depends on what security mechanism is in place)


## Design

I don't have a clear predefined picture of the design. I have vague notions of appropriate separation of concerns, and I'm guided by those. So I try to keep the attendance.controller package clean in that it should only contain code that relates to Spring and the web. If you need to write any business logic, push it down into attendance.service. Don't access attendance.repository from the attendance.controller layer - route all access via business layer in attendance.service. Put all model classes (and nothing else) in attendance.model.

### Thymeleaf (front end)

Take a look at the templates in src/main/resources/templates. You should be able to get a feel for Thymeleaf from that.

## Implementation notes

Note that I strongly prefer the annotation-based approach to Spring development - no beans.xml with shedloads of unvalidated declarations that can only fall in a heap at runtime. Instead, as annotations by definition decorate the code they affect, you can more clearly see what's going on, all in one place. Yes, that means my controllers can only be used with Spring. On the day that I consider migrating the project away from Spring, I'll worry about that.. 

## Building

`gradlew build`

To create a project suitable for import into Eclipse:

`gradlew eclipse`

## Running

### Configuration

Configuration is minimal. Since you run the application from Gradle, you'll need a shell in which you can run gradlew. In that shell you'll need to set SPRING_PROFILES_ACTIVE=dev. As well as setting appropriate environmental properties, this causes some sample data to be added (see attendance.Application), since we have a configuration with an in-memory database. *DON'T* set this in an environment where unit tests are going to be run - they depend on data that they set up themselves.

`gradlew bootRun`

## Deployment

I set up a free account with Pivotal Web Services (PWS) which allows me to deploy the application to the cloud with a single command (again, via Gradle).

`gradlew war cfPush`

## Further reading

[Spring documentation](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/)
[Spring boot documentation](http://docs.spring.io/spring-boot/docs/1.2.2.RELEASE/reference/htmlsingle/)
[Thymeleaf tutorial](http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html)
[Thymeleaf with Spring](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)

## Tips and tricks

* You should be able to treat Thymeleaf templates as regular HTML files and open them in your browser
* localhost:8080/configprops shows you the configuration that Spring Boot has created for you
* localhost:8080/mappings shows you the URL mappings that exist

## Wrinkles

This is a work in progress. There's a large number of things that are currently broken or suboptimal. I could have waited until it was perfect, but that could be the twelfth of never. If you fix any of the items below, please strike them off the list:
* application properties are defined in application.properties; per-profile stuff is in application_${profile}.properties. This is nasty because it gets built into the war. Would prefer to reference an external file, but can't find how to do this so it works on PWS.
* same goes for logging, in logback.xml. And I'd like to have per-profile logging setup
