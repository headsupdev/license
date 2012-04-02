HeadsUp Development License SDK
===============================

A library for managing licenses in Java software. Contains code to manage licenses in your software as well as tools to manage your keys and license files.

## Features

* Built in support for expiring based on usage or end date
* Built in support for licensing a version range
* Highly extensible for any custom attributes
* Secure data for encrypting license details

## Example usage

Generate keys for first usage

    mvn exec:java -Dexec.args="genkeys"

Then we can create a basic license - here you would like to override the main create() method to add your own properties

    mvn exec:java -Dexec.args="create"

Copy the text after "Created license: " - this is your license key :)   
Then you can test the license created, and see any custom content by executing:

    mvn exec:java -Dexec.args="test"

And paste in the copied license string...


## Planned Features

We are currently working on a management UI that makes managing licenses and creating custom license properties much easier - stay tuned!

