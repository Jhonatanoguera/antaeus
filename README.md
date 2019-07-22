## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will pay those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

### Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── pleo-antaeus-app
|
|       Packages containing the main() application. 
|       This is where all the dependencies are instantiated.
|
├── pleo-antaeus-core
|
|       This is where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|
|       Module interfacing with the database. Contains the models, mappings and access layer.
|
├── pleo-antaeus-models
|
|       Definition of models used throughout the application.
|
├── pleo-antaeus-rest
|
|        Entry point for REST API. This is where the routes are defined.
└──
```

## Instructions
Fork this repo with your solution. We want to see your progression through commits (don’t commit the entire solution in 1 step) and don't forget to create a README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

Happy hacking 😁!

## How to run
```
./docker-start.sh
```

## Libraries currently in use
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library

## Development log

Alright, as requested, this README will work as a sort of log where I'll be noting up the process of
developing the Antaeus challenge.

First, a quick self reminder to fix the UNIX line ends for running the app on Windows

* I've been using Windows for over a year now as the OS where I generally code, noticed Docker
has some issues and requires a specific version of windows 10 to work locally unless I use the
docker toolbox, sadly, never been able to enable virtualization on this pc despite being supported.
Moved to my Linux partition which hasn't been touched for over a year now and more errors popped up
based on my Linux partition state. Unluckily, I've dropped the idea on fixing it and requested some help
which is now allowing me to properly run the docker and the application!

* A day ago, I started thinking on how to tackle the problem and although it wasn't really
difficult to find a mental solution or a guideline to follow for resolving the challenge. I spent a
little more time debating in the approach and reading through the code in the application. First
thoughts are how compact Kotlin code looks like and really liked the idea of typing most variables
instead of letting the compiler assume what is the variable type. Due to my Java background, this
makes me feel a little more safe.

Checked the documentations and examples for some of the libraries like Javalin and Mockk. Focused
mostly on Javalin as my first small goal was fetching the issues marked as pending and creating a
rest endpoint for displaying the results.

The proposed solution for solving the challenge is going for the smaller picture to the bigger. The
application has a lot of space to increase its functions, add safety measures, add unit tests to
create a more robust application. The path to follow consists on:

1 - Fetching & Displaying PENDING issued invoices:
One of the many reasons why I love being a programmer, is seeing. Generally, clients, coworkers and
3rd parties enjoy seeing advancements. Although there's not a formal UI, we can display the
invoices to keep track and see progress. It doesn't only motivates on seeing the small steps but
also help visualizing the progress on the path.

2 - Add single invoice payment method:
This is the most basic function required. The reason to create a function for a single invoice and
not all of them at once is to minimize errors and make code that's friendly for creating an unit
test. We don't know who can work in code we make later and it's our responsibility to make our code
understandable.

3 - Create a task for paying a list of Pending invoices
Just the bigger picture of the previous step. Note that so far there are no validations done, as we
want to have functional code that solves the punctual challenge.

4 - Create a task to track the date time and execute the point 3 task
One of the requirements is to pay the pending invoices at the 1st of the month. Unlike Java, I do
not have a lot of knowledge in Kotlin and it can get trickier to do this. Although so far from what
I've read and seen about this language, not only finding the date shouldn't be too difficult but
also open possibilities to add localization to the task.

5 - Validations!
With the previous point finished, we're now technically done with the challenge. Except for the
exception handling, no pun intended. At this point, it's just refining the code and handling the
exception that can appear through the execution.

6 - Going wild.
Basically letting my imagination flow and open new functions to Antaeus. Example, allowing the
 database to update invoices after a successful payment. Adding a limit date field to the Invoice
 model, as honestly, you should be given a few days to pay an invoice before having issues with this to
 evade problems when a client is having X or Y issue. Based on the previous one, it should also try
 to inform the customer about a failure in the payment process with their invoice and also the app
 should try more than once to pay in case there's an issue with the connection which can cause an
 unfair issue for a customer. Lastly, unit testing, not having a lot of experience as I'm close to a
 year and half after obtaining my engineering degree, I've had to deal with unit testing for a good
 amount of times in the workplaces I've been in order to understand how important this is and
 understand the code done. Although some have given me a run for my money, I still appreciate an
 unit test as it keeps the code safe and in check.

So far! step one has been completed with a little time of tweaking around with Javalin and a first
attempt to create a general function to retrieve only pending invoices. It was later modified to a
more general funtion, overriding the same fetchInvoices() function while accepting a status and
retrieving the invoices based on it. This will help in case there are plans for adding new status,
example could be adding a "Failure" status which would either result on a phone call/ mail being
sent to contact the customer. Little random note, I have really enjoyed Kotlin so far, specially the
 Elvis operator and the reason why it was named like that, hoping to see if I can use it somewhere
haha.

* Alright, back to work after a small delay, continued reading more regarding kotlin logging and
scheduling for tasks, got done the work for a single ticket and general payment, exceptions haven't
been implemented yet. However, I'm not 100% happy with the current single pay method, tweaked a bit
with the charge function, added a rest endpoint to test simple validations and there seem to be
cases where the criteria set (simply return it was paid if the amount was > 150) was true but the
ticket didn't pay. More likely will not directly pass the given invoice in case it's wrong. The
ideas so far include to either create a safe copy to transform or create a new function to update
the value based on the given result from the charge() function.

A quick google also revealed most java.Stream functions can be used with Lists in Kotlin and
couldn't help but smile. Streams have been one of the Java 8+ characteristics that I've learn to
love due to how easy many process can be achieved faster and with less resource consumption.
Iterating through the list of invoices ready to pay was simple.

Another function I'll be adding soon is a more general pay-in-bulk for the automated list of
invoices, as the current behavior is executing the function when accessing the rest endpoint.
Incoming automatedPaymentOfPendingInvoices() which simply takes off the previous functionality from
the endpoint and keeps it in the bilingService. Hopefully with have done the schedulerService which
will be the class that fires the automated payment on th 1st of the month, using the
ScheduledExecutorService as it seems to be a slightly better option compared to Timer.

* A quick update on the first refactoring branch, after reading for a bit regarding exception and
variables as statements, understood how to write a shorter and simpler function. Refactored the
bulkInvoicePayment as a general function to deal with invoice lists. Created an specific function
for pending invoices and updated the rest endpoint to use the previous function. Now, time to work
in the scheduling function.

* Added a new service for a runnable task using a schedulerExecutorService. After continuing
reading, it seems like Timer could have been easier to implement. Same as using Calendar for the
current date, however, using LocalDate could open possibilities to more accurate information. Note
the time could also be done with LocalDateTime in case we wanted to set a specific time when to run
our automated payment method. One thing to point is I'm not 100% sure LocalDate uses a localized
date/time. Calendar seems to already be localized which is something I'd like to study more before
changing my mind about LocalDate. Another consideration is that LocalDate is recommended as
it's part of Java's time and date libraries updated for the Java 8 version which I personally
enjoyed working with. Lastly, the AntaeusApp,kt has been updated to run a TaskSchedulerService and
start the scheduler task to be executed.

A few things worth nothing. I am much more comfortable after the refactoring, having that little bug
going around your mind when you are not satisfied with something made it easier to continue the
work. I should also point out about my dire need to quickly solve the previous functionality for
the payment of invoices was mostly due to being used to create test-friendly code. I do am aware of
not implementing a single unit test yet, however, I've been considering how though would be testing
the functions, I'm happy with the current results as in a simple Java/Junit/Mockito environment,
it'll be simple and not require several workarounds. The main object to mock would be the data
access and services to work with. I expect to implement some unit tests in the upcoming days. As of
now, the tiered development process is done with the 4th step. Moving onto the 5th.

One little thing, I'm used on creating new branches for the new features to work at and I shall
apologize in case someone received a notification regarding a PR. Haven't used GitHub in some time
and forgot to select my fork as the base, oops :x

* Next update, added exception handling which is technically the last step of the project. Also
enabled the logger I had set in the billingService now that we can expect some sort of exception
handling. While finishing this exceptions, I decided to add a single invoice payment function which
receives an Id. Why? Because when exceptions are thrown, we might find in the final list invoices
that weren't processed. I thought it could be useful to have an option where you could input an Id
and have the invoice processed in case just one fails. It might be faster than waiting for an
scheduled task to be executed again in case there are failures. And it isn't only done for this
single reason. Reading about Pleo, I found there are customers who communicate in Spanish. As
someone from latino america, payments via internet aren't being used as much as they should
considering all the existing security mechanisms. Many aren't that fond of online payments and
prefer to form a line, then pay personally. Although I share the idea of just automating this type
of payments, you can also find yourself in a position where you want to pay an invoice manually for
any reason.  It's a very inexpensive function which can brighten the mood of a group of users. This
can be considered as the starting point for the 6th point

* And another update, this time, I explained my mom what Antaeus did and asked for input regarding
what functionalities would make her happy if she had to use Antaeus. Truth is, most were UI based
requests lol, however, from some of her ideas I got a couple good ones that can be implemented or
partially. First, the one which can be the biggest, add non-monthly payment options. It is achieved
partially as of this log, Customer model has been updated to receive a membership type and a class
of MembershipType has been created with some options of periodic payment(I am not happy about
using Trimester AND Quarter payment. Both of thee aren't needed but I sort of realized the
redundancy after having the updates almost done).

After adding this new field, tables and mappings were updated to handle this and for quick "testing"
purposes, two rest endpoints were created. Next, a function to add a random membershipType
was created to populate the database correctly. Another function to retrieve a list of invoices
based on the pending status and the membershipType was created. The proper data access layer
function has been also implemented and the rest endpoints are functional with the previous
mentioned functions. The scheduled task for these periodic membershipTypes haven't been created yet.
 A joinDate field is required for the customer as this type of memberships are executed from the
 join date to the specified membership plan.

Another idea to be implemented is adding an account "overflow". Sometimes, when doing manual
payments, people might want to pay part of the next month's invoice in advance. Adding an overflow
field to the customer, would deduct from the next month's invoice. I thought of this as a good idea
which is mostly used by public services over here. Finally, as previously mentioned, email field on
the user to create an emailing function in case one of the exceptions are triggered during the
automated payment. Another neat use of the email field would be emailing a few days before the
automated payment is done, this, to remind the user in case they need to add money to their account.

I have also discarded the idea of giving a "maximum pay period" due to the SaaS model business.
Since you're paying for services in an estimated range of time, is not the best practice to allow a
few additional days to renew your subscription. This idea will be replaced by the payment reminder
to implement. Now, I'll get into working on some general tests for the application

* Okay, last entry on this readMe. First, I have decided to discard the remaining additional ideas
as they aren't really part of the challenge scope. Having no set time to finish, personally, seems a
little more concerning to me than having a set deadline as I cannot really measure what could be a
fair time to send the challenge. It did however, give me time to read more about kotlin, mockk,
Docker, Javalin and other tips and tricks for Kotlin. Previously to the challenge, I had ventured on
 reading about the Kotlin language, look for why people liked using and why was it gaining so much
 support from developers in general. Being relatively new to this industry, I do hope my Antaeus
 fork can be kept on git for future references on Kotlin experience :) This challenge, did give me
the final push I needed to dive deep into the language, understand and be able to create something
with it. The learning curve wasn't that hard to me due to my Java background. Truly, I found Docker
a little more confusing as I jumped straight into installing and trying to run the app lol. So, I'm
thankful for being pushed to learn a couple more technologies.

I personally, consider I took quite a good amount of time. The challenge was fairly simple but I
think I took a very conservative approach wanting to soak myself in the basics of everything and a
little more before tackling the problem in general. It could be done in less than a work day if you
aim straight to reach the goal, however and not wanting to excuse, July was a moved month for me and
the previous weekend I wasn't really available to tackle the challenge. No regrets using my nights
to work through the challenge even if I had to be coffee-powered the next day as it was very fun to
me. Also, daily productivity wasn't affected dw, I enjoy developing software, this makes it easy
to zone out and lose track of the time. However, there's one only little thing I wasn't fond of
regarding kotlin. I generally like limiting the line length whenever I'm coding, so, sometimes my
IDE would automatically break down a line and the instruction was marked as unknown. Resulting in
spending some time searching around if I was missing an import or if there was some issue until
realizing Kotlin needs the statements to be in the same line to "chain" them. That is, of course, a
personal preference but the experience overall was fantastic.