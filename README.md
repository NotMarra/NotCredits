![GitHub release (with filter)](https://img.shields.io/github/v/release/NotMarra/NotCredits) [![CodeFactor](https://www.codefactor.io/repository/github/notmarra/notcredits/badge)](https://www.codefactor.io/repository/github/notmarra/notcredits) ![GitHub License](https://img.shields.io/github/license/NotMarra/NotCredits) ![GitHub issues](https://img.shields.io/github/issues/NotMarra/NotCredits) [![](https://jitpack.io/v/NotMarra/NotCredits.svg)](https://jitpack.io/#NotMarra/NotCredits)



# What is it?

A plugin that solves all your economy problems! A custom economy that doesn't need Vault or other plugins!


# Getting the dependency

## Maven

Repository:

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

Dependency:

    <dependency>
	    <groupId>com.github.NotMarra</groupId>
	    <artifactId>NotCredits</artifactId>
	    <version>v2.0</version>
	</dependency>

## Gradlew

Repository:
```
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```
Dependency:
```
dependencies {
  implementation 'com.github.NotMarra:NotCredits:v2.0'
}
```

# API Documentation

## Methods

### `setBalance(String uuid, double amount)`

Sets the balance for a player identified by their unique UUID.

- **Parameters:**
  - `uuid`: Unique identifier for the player.
  - `amount`: The amount to set as the player's new balance.

---

### `getBalance(String uuid)`

Gets the balance for a player identified by their unique UUID.

- **Parameters:**
  - `uuid`: Unique identifier for the player.
  
- **Returns:**  
  - The balance of the player.

---

### `setBalanceByName(String name, double amount)`

Sets the balance for a player identified by their name.

- **Parameters:**
  - `name`: The name of the player.
  - `amount`: The amount to set as the player's new balance.

---

### `getBalanceByName(String name)`

Gets the balance for a player identified by their name.

- **Parameters:**
  - `name`: The name of the player.
  
- **Returns:**  
  - The balance of the player.

---

### `getBalanceByOrder(int order)`

Retrieves the balance of a player based on their position in the balance ranking.

- **Parameters:**
  - `order`: The rank position of the player (e.g., 1 for the top player).
  
- **Returns:**  
  - The balance of the player.

---

### `getTopPlayerName(int order)`

Retrieves the name of the player based on their position in the balance ranking.

- **Parameters:**
  - `order`: The rank position of the player (e.g., 1 for the top player).
  
- **Returns:**  
  - The name of the player.

---

### `getTopPlayersWithBalance(int position, int amount)`

Retrieves a list of players and their balances starting from a specified rank position.

- **Parameters:**
  - `position`: The starting rank position.
  - `amount`: The number of players to retrieve.
  
- **Returns:**  
  - A list of maps containing player names and their balances (`List<Map<String, Double>>`).


<a href='https://ko-fi.com/V7V7GHBU0' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://storage.ko-fi.com/cdn/kofi1.png?v=3' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>
