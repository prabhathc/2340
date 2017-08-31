This application supports the GT Sustain/Learn/Serve initiative described here: external link: https://serve-learn-sustain.gatech.edu/ . You may actually name your application whatever you want, I will be calling it U Dirty Rat.

Many US cities saw a surge in rodent complaints beginning in 2015. Sanitation departments blame the increase on milder winters.

This is important because rats carry over 35 diseases that can be contracted directly (through bites) or indirectly through fleas and ticks. They also cause serious damage to private and civic properties by gnawing on structures such as electrical lines and plumbing. They spoil community gardens and contaminate food supplies (not to mention that Bob finds them really icky).

We want to help city officials answer questions like:

How many rodent sightings were there over the last X years.
Where are the most heavily infested areas.
What types of locations (hospital, residental, business building) have the most sightings
What time of day do most sightings occur.
We also want to use the application to provide predictions of future sightings based on the characteristics of past sightings.

That is where this app comes in.

User Categories
Admin : An admin can add and remove users and unlock accounts.
User : A user is anyone who wants to use the system to view data, or enter new sightings.
All user types contain the same basic information:

login name (in recognition of current popular trends, this can be the email address)
password
account state (locked or unlocked)
contact info (email address)
Rat Data Handling.
We have access to the actual New York City rat sighting database. A subset of the data will be provided to you in the form of a comma separated file (CSV).

Rat Reporting.
The required information to fill out a rat sighting report includes the following:

unique key (auto-assigned by system)
location (latitude and longitude)
date and time
location type (select from: 1-2 Family Dwelling, 3+ Family Apt. Building, 3+ Family Mixed Use Building, Commercial Building, Vacant Lot, Construction Site, Hospital, Catch Basin/Sewer)
Incident zip code
Incident address
City
Borough ( select from: MANHATTAN, STATEN ISLAND, QUEENS, BROOKLYN, BRONX)
Security
A person must login to the application in order to access its features. At login, the user category is determined and the appropriate rights granted.

If a login attempt is unsuccessful, the person is allowed a total of 3 failures before they are locked out. An administrator must unlock their account once it is locked.

Searches
A user may conduct a search for information about rat sightings. The following searches should be supported:

Date Range - show all sightings by month for the given range
Borough - show all sightings for the selected borough
Location Type - show all sightings for the selected location type.
Map Displays
The location of rat sightings should be shown on google maps. Clicking on a pin should show some details of the sighting report at that location.

Scenarios
(No wording here should be construed as requiring you to design the system in a particular way, it is just to illustrate some uses of the system)

1. Bob is lounging outside his apartment in downtown New York. He sees a rat the size of a small dog. He pulls out his trusty smart phone and fills out a sighting report.

2. Sally is a supervisor for the sanitation department. She pulls out her trusty smart phone and opens the U Dirty Rat application. She queries the system for sightings in the last 24 hours. She puts the results into google maps and notices a lot of pins around Bob's apartment. She dispatches an extermination team to take care of the rats.