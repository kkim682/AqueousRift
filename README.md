[![Stories in Ready](https://badge.waffle.io/joshuagaul/aqueous-rift.png?label=ready&title=Ready)](https://waffle.io/joshuagaul/aqueous-rift)
# Aqueous Rift

<a href="https://www.youtube.com/watch?v=tVMG9emm2Rw" target="blank"><img src="http://img.youtube.com/vi/tVMG9emm2Rw/0.jpg"
alt="Aqueous Rift" width="240" height="180" border="0" /></a>
<br>[Watch introduction →] (https://www.youtube.com/watch?v=tVMG9emm2Rw)
<br>
<br>Process: https://github.com/joshuagaul/aqueous-rift
<br>Created by: [Graham McAllister],  [Ahjin Noh], [Joshua Gaul], [KwangHee Kim], [Aakanksha Patel]
<br>*Requires GMapsFX-2.0.9.*
<br>*You must ask for a configuration file to build and run this app.*

### Project Description
(cited from CS2340, Georgia Tech)

In many parts of the world, access to clean water is a severe problem. Many times, water is available, but people do not know where to get it. Other times, water may appear clean, but has unacceptable levels of contaminants.
Even in areas where there is a lack of clean water, the population has access to devices on which applications can run. Our JavaFX application, Aqueous Rift, will allow water reporting to crowd sourced. People can report on locations where water is available. Users of the application can find the nearest water source or report on a new water source. Workers with test kits will also be able to report on contaminant levels. The application will provide historical graphs to show variations in water quality over time for a specific location.

### Features
**Authorization Levels:**
* All Users (including guests) may view available water sources. They may submit a report on water source when logged in.
* Workers can confirm source report and report on water purity levels.
* Managers can do everything workers can do, and can view historical reports. They can also delete individual reports that deem inaccurate.
* Administrators works with maintenance of the system, including deleting accounts, banning a user from logging in or submitting reports, and viewing security logs.

**Security**
* Login: All registered users login to be authorized to accomplish the appropriate tasks. Users may be blocked after 3 incorrect login attempts.
* Find password: Users may recover password using texts or answering security questions.
* Logging: Security log keeps track of login attempts, account deletion, user ban/unban, and report submission/update/deletion.

**Report Data**
* Source report: consists of date, time, autogenerated report number, username, location, type of water, and condition of water.
* Purity report/Confirmed report: consists of date, time, autogenerated report number, username, location, overall condition, virus ppm, and contaminant ppm.
* Water availability: clickable pins displayed on google map, which shows report information. Pins can be filtered by water type/condition categories. All water availability can be accessed on a report table.
* Historical Report: manager may enter a location, year, and virus/contaminant to view XY graph of water purity history.

[Graham McAllister]: <https://github.com/gmcallister3>
[Ahjin Noh]: <https://github.com/ahjinnoh>
[Joshua Gaul]: <https://github.com/joshuagaul>
[KwangHee Kim]: <https://github.com/kkim682>
[Aakanksha Patel]: <https://github.com/aakanksha1998>
