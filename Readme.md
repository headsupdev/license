HeadsUp Development License SDK
===============================

A library for managing licenses in Java software. Contains code to manage licenses in your software as well as tools to manage your keys and license files.

## Features

* Securely licensing software and controlling features and limitations
* Support for licenses expiring based on an end date or a period of usage
* Licensing a version range for disallowing major updates with the same license
* Highly extensible for any custom attributes
* Secure data for encrypting and validating license details

## How it works

HeadsUp Licenses use 3 keys for secure licensing, data transmission and validation.
When you generate license keys you will find a private key, public key and shared key.
The private key is used to sign the license, the public key to validate it and the shared key is used to encrypt the data.

When you create a license the following operations are performed:

1. A digest is taken of the license data
1. The digest is encrypted using the private key
1. The data and encrypted digest are combined
1. The whole license is encrypted using the shared key and encoded to plain text

This creates a license that can be copied and pasted wherever required

When a license decode request is performed the following steps are executed

1. The license is decoded and decrypted using the shared key
1. The data is extracted and saved
1. The digest is decrypted using the public key which verifies the source of the license
1. The digest is checked against the data to ensure it has not changed

This means that a validated license can be guaranteed to have come from you (as long as you don't leak your private key)
and that the data has not been interfered with in transit.

## Example usage

### Setting up

Generate keys for first usage

    java -jar license-1.0.jar gen-keys
    java -jar license-1.0.jar base64

This creates 3 keys in the directory ~/.license (unless overridden with the -configdir option).
It also creates 3 base64 encoded files for use in your code when setting up a license decoder.

__NOTE let your private key or base64 encoded private key be copied and don't add it to your software!__

### Incorporating in your software

To use the license in your code you should add a few simple lines of code, such as:

    LicenseDecoder decoder = new LicenseDecoder();
    decoder.setPublicKey( LicenseUtils.deserialiseKey( config.getPublicKeyFile() ) );
    decoder.setSharedKey( LicenseUtils.deserialiseKey( config.getSharedKeyFile() ) );

    try {
        License out = new License();
        decoder.decodeLicense( license, out );
    } catch ( LicenseException e ) {
        System.err.println( "License decoding failed..." );
        // handle however...
    }

### Creating a license

Then we can create a basic license by default it asks who to license to - here you would like to override the main create() method to add your own properties

    java -jar license-1.0.jar create

Copy the text after "Created license: " - this is your license key :)   
Then you can test the license created, and see any custom content by executing:

    java -jar license-1.0.jar test

And paste in the copied license string...

Assuming this has passed you can send the license to your client and have them load it into the software (this could
be through pasting it into a UI or saving it to a file, depending on your implementation).

## Custom licenses

Custom licenses are fully supported and simply need to extend the License class and add fields that delegate to
the getProperty and setProperty methods to serialise and deserialise the state. Remember you will need to customise
the creation code and also alter the code in your app to pass an instance of your license into the decodeLicense method call.

More documentation to follow...

## Work in Progress

The license library is completely capable of controlling access to features based on license files and has been used in live software in a production environment.

**However** The project has much ongoing work, most of which is focussed around creating a complete GUI for managing licenses.
This is available in the project now as the default action when the jar is loaded but it cannot yet create licenses based on custom code - much more to come.

For now just execute

    java -jar target/license-1.1-SNAPSHOT.jar

