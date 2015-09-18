# About
Star is a web first, multi-agent framework built on the JVM implemented using the [Play framework](playframework.com), with [Golem](http://www.cs.rhbnc.ac.uk/home/kostas/pubs/debs09.pdf) as its agent runtime.
Star allows agents to communicate [RESTfully](https://en.wikipedia.org/wiki/Representational_state_transfer) with external web services, other agents and even users. 
# Taxonomy
The Star project is composed of 4 sub-projects.
##### Star Platform
This is the main runtime for a Star environment. It offers a restful API and the facility to deploy and run new agents and containers. External container communications are currently a work in progress.
##### Star Core
This is the root dependency for Star, and contains all of the underlying code for Golem agents to interoperate. 
##### Star SDK
This is a test harness, allowing developers to test and run basic multi-agent applications. Unfortunately the SDK currently does not expose a Restful api.
##### Star Examples
This project contains a set of example applications which can be deployed into a running Star Platform.

# Install
Star is installed with [Docker](https://www.docker.com/). 
If you don't have Docker installed, see the instructions [here](http://docs.docker.com/mac/started/).
These entail:
- Installing the Docker Toolbox
- Opening the Kitematic GUI
- Creating a Docker Hub account (optional)

In the UI, search for 
```sh
noddybear/star 
```.
Click 'create' to spin up a new STAR System. Click on the now running container on the left side of the UI. To the right of 'IP & PORTS', click the Cog icon. This should present you with 2 ports. One is the _local_ Docker port, namely, the port the STAR System is running on inside the Docker container. To the right of that is the _global_ IP address and port. This is the location that you can use to access STAR.


> If you don't want to install the Docker Toolbox, you can do the following: As Docker containers are only supported on Linux, if you're using a Mac or Windows, install [Boot2Docker](http://boot2docker.io/). Boot2Docker is a lightweight Linux virtual machine which encapsulates a single Docker container.

Open a new Web Browser window and type in the _global_ location, like:
```http://192.168.99.100:32770/v1/welcome/tutorial-agent```.
(Obviously with the correct port and IP address.)

This should now display the following text: ```Hello World From within a dynamic view```.
Success! You've just sent a HTTP request to your first agent, and it says hello.

# Running your first application
### Dashboard
The Dashboard is the user interface to the STAR API, allowing a user to manage their running agents and containers, as well as deploy new ones. 
Navigate to: ```http://192.168.99.100:32770/containers``` to access the Dashboard.

### Cleaning Robot Application
The Cleaning Robot project will be our HelloWorld application.
- Download/pull the master branch of the Star Examples project
- Navigate
