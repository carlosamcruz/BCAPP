# BCAPP
Template for Android Application using the BSV Blockchain

This template includes classes that enables the creation and use of Bitcoin legacy wallets from a hexadecimal private key.
This wallet is able to handle compressed and uncompressed BSV addresses and deal with OP_RETURN data.
The codes of all JAVA classes of the project are open, nevertheless they are still poorly documented, and it is not neatly written as well.
Though template worked well on our tests, we do not warrant it to be free of bugs or any malfunctionality whatsoever. Therefore, use at your own risk.

The set of classes employed in this template project includes:

Ecc.java - Ellipitic curve secp256k1;
EcdsaSecretus.java - ECDSA based on secp256k1 elliptic curve;
SHA256G.java - SHA256 hash function with methods to deal with different input/output types;
TonelliShanks.java - to deal with some Ellipical Curves operations
Ripemd160.java - RIPEMD160 hash function to deal with bitcoin wallets;
Keygen.java - Classe to deal with private/public generation and encoding, gernation of BSV wallets, SEC public key encode and decode, DER ECDSA singature encoding and decode.
