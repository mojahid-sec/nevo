package com.example.data.localization

object NivoStrings {
    // Navigation
    fun navHome(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোম" else "Home"
    fun navCalendar(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্যালেন্ডার" else "Calendar"
    fun navGoals(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লক্ষ্য" else "Goals"
    fun navSettings(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সেটিংস" else "Settings"

    // Greetings
    fun greeting(lang: AppLanguage, name: String?): String {
        return if (lang == AppLanguage.BANGLA) {
            if (name.isNullOrBlank()) "আবার স্বাগতম!" else "আবার স্বাগতম, $name!"
        } else {
            if (name.isNullOrBlank()) "Welcome back!" else "Welcome back, $name!"
        }
    }

    fun appTagline(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতিদিন এক ধাপ এগিয়ে।" else "One Day at a Time."
    fun homeSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিজের শৃঙ্খলায় অবিচল থাকুন।" else "Build discipline, one day at a time."

    // Check In & Countdowns
    fun checkInButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "চেক ইন" else "Check In"
    fun checkedInForToday(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আজকের চেক-ইন সম্পন্ন" else "Checked In for Today"
    fun checkInReady(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আজকের চেক-ইন প্রস্তুত। উপরে ট্যাপ করুন!" else "Today's check-in is ready. Tap Check In above!"
    fun nextCheckInIn(lang: AppLanguage, hours: Long, minutes: Long, seconds: Long): String {
        return if (lang == AppLanguage.BANGLA) {
            "পরবর্তী চেক-ইন বাকি: ${hours.toLocalizedDigits(lang)} ঘণ্টা ${minutes.toLocalizedDigits(lang)} মি. ${seconds.toLocalizedDigits(lang)} সে."
        } else {
            "Next check-in in: ${hours}h ${minutes}m ${seconds}s"
        }
    }

    // Progress
    fun manualProgressTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ম্যানুয়াল অগ্রগতি" else "Manual Progress"
    fun automaticProgressTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "স্বয়ংক্রিয় অগ্রগতি" else "Automatic Progress"
    fun manualProgressDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "চেক-ইন বোতামের মাধ্যমে অর্জিত" else "Recorded via manual Check In"
    fun automaticProgressDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতি ২৪ ঘণ্টা সময়ের সাথে বৃদ্ধি পায়" else "Advances automatically every 24 hours"

    // Tracking Modes (Option A & Option B)
    fun trackingModeLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ট্র্যাকিং মোড" else "Tracking Mode"
    fun optionAManual(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অপশন এ: ম্যানুয়াল ট্র্যাকিং" else "Option A: Manual Tracking"
    fun optionBAutomatic(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অপশন বি: স্বয়ংক্রিয় ট্র্যাকিং" else "Option B: Automatic Tracking"
    fun manualModeShort(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ম্যানুয়াল" else "Manual Tracking"
    fun autoModeShort(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "স্বয়ংক্রিয়" else "Automatic Tracking"
    fun manualModeDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতি ২৪ ঘণ্টায় একবার চেক-ইন বাটন চেপে অগ্রগতি রেকর্ড করতে হবে।" else "Press the Check-in button once every 24 hours to record progress."
    fun autoModeDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো বাটন চাপার প্রয়োজন নেই। প্রতি ২৪ ঘণ্টায় স্বয়ংক্রিয়ভাবে অগ্রগতি বাড়বে।" else "Calculates automatically from start time. Advances 1 day every 24 hours without check-in."
    fun autoModeActiveNotice(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "স্বয়ংক্রিয় মোড সক্রিয় — প্রতি ২৪ ঘণ্টায় অগ্রগতি স্বয়ংক্রিয়ভাবে বৃদ্ধি পায়।" else "Automatic Mode Active — Progress increases every 24 hours without check-in."
    fun nextAutoDayCountdown(lang: AppLanguage, hours: Long, minutes: Long, seconds: Long): String {
        return if (lang == AppLanguage.BANGLA) {
            "পরবর্তী স্বয়ংক্রিয় দিন বাকি: ${hours.toLocalizedDigits(lang)} ঘণ্টা ${minutes.toLocalizedDigits(lang)} মি. ${seconds.toLocalizedDigits(lang)} সে."
        } else {
            "Next automatic day in: ${hours}h ${minutes}m ${seconds}s"
        }
    }
    fun optionalManualCheckIn(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ঐচ্ছিক ম্যানুয়াল চেক-ইন" else "Optional Manual Check In"

    fun ofDays(lang: AppLanguage, completed: Int, total: Int): String {
        return if (lang == AppLanguage.BANGLA) {
            "${total.toLocalizedDigits(lang)} দিনের মধ্যে ${completed.toLocalizedDigits(lang)} দিন সম্পন্ন"
        } else {
            "$completed of $total Days"
        }
    }

    fun daysRatio(lang: AppLanguage, completed: Int, total: Int): String {
        return if (lang == AppLanguage.BANGLA) {
            "${completed.toLocalizedDigits(lang)}/${total.toLocalizedDigits(lang)} দিন"
        } else {
            "$completed/$total days"
        }
    }

    fun daysRemaining(lang: AppLanguage, remaining: Int): String {
        return if (lang == AppLanguage.BANGLA) {
            "${remaining.toLocalizedDigits(lang)} দিন বাকি"
        } else {
            "$remaining days remaining"
        }
    }

    // Empty States
    fun noActiveGoal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো সক্রিয় লক্ষ্য নেই" else "No Active Goal Yet"
    fun noActiveGoalDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "একটি লক্ষ্য নির্ধারণ করুন এবং প্রতিদিন শৃঙ্খলা গড়ে তুলুন।" else "Set a goal and build lifelong discipline one day at a time."
    fun createFirstGoal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রথম লক্ষ্য তৈরি করুন" else "Create Your First Goal"
    fun loadSampleGoal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নমুনা ৩০ দিনের লক্ষ্য লোড করুন" else "Load sample 30-day goal"

    // Goal Management
    fun myGoals(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আমার লক্ষ্যসমূহ" else "My Goals"
    fun newGoal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন লক্ষ্য" else "New Goal"
    fun editGoal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লক্ষ্য সম্পাদনা" else "Edit Goal"
    fun activeOnHome(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোম স্ক্রিনে সক্রিয়" else "Active on Home Screen"
    fun startDateLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "শুরুর তারিখ" else "Start Date"
    fun durationLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মেয়াদ" else "Duration"
    fun daysUnit(lang: AppLanguage, days: Int) = if (lang == AppLanguage.BANGLA) "${days.toLocalizedDigits(lang)} দিন" else "$days Days"
    fun completedLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সম্পন্ন" else "Completed"
    fun remainingLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অবশিষ্ট" else "Remaining"
    fun progressLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অগ্রগতি" else "Progress"
    fun expectedCompletionLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রত্যাশিত সমাপ্তি" else "Target Date"
    fun streakLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ধারাবাহিকতা" else "Streak"

    // Form Fields & Validation
    fun goalTitleLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লক্ষ্যের শিরোনাম *" else "Goal Title *"
    fun goalTitlePlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "যেমন: ৩০ দিন নিয়মিত বই পড়া" else "e.g. 30 Days of Morning Workout"
    fun goalDurationLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লক্ষ্যের মেয়াদ (২ থেকে ৩৬৫ দিন)" else "Goal Duration (2 to 365 Days)"
    fun personalReasonLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ব্যক্তিগত কারণ (ঐচ্ছিক)" else "Personal Reason (Optional)"
    fun personalReasonPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এই লক্ষ্যটি আপনার কাছে কেন গুরুত্বপূর্ণ? কঠিন দিনে কী আপনাকে অনুপ্রাণিত রাখবে?" else "Why is this goal important to you? What will keep you disciplined on tough days?"
    fun saveGoalButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সংরক্ষণ করুন" else "Save Goal"
    fun updateGoalButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপডেট করুন" else "Update Goal"
    fun cancelButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বাতিল" else "Cancel"
    fun deleteButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মুছুন" else "Delete"

    // Validation Errors
    fun errorTitleRequired(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অনুগ্রহ করে লক্ষ্যের শিরোনাম লিখুন" else "Please enter a goal title"
    fun errorDurationInvalid(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মেয়াদ অবশ্যই ২ থেকে ৩৬৫ দিনের মধ্যে পূর্ণসংখ্যা হতে হবে" else "Duration must be an integer between 2 and 365 days"
    fun errorDurationTooSmall(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মেয়াদ কমপক্ষে ২ দিন হতে হবে" else "Duration must be at least 2 days"
    fun errorDurationTooLarge(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মেয়াদ ৩৬৫ দিনের বেশি হতে পারে না" else "Duration cannot exceed 365 days"

    // Goal Completion
    fun goalCompletedTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লক্ষ্য অর্জিত হয়েছে!" else "GOAL COMPLETED"
    fun goalCompletedMessage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অসাধারণ অধ্যবসায়! আপনি প্রতিদিন এক ধাপ করে আপনার লক্ষ্য সফলভাবে সম্পন্ন করেছেন।" else "Outstanding perseverance! You stayed disciplined and completed your goal one day at a time."
    fun totalCheckInsLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মোট চেক-ইন" else "Total Check-ins"
    fun completionDateLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সমাপ্তির তারিখ" else "Completion Date"
    fun returnToHomeButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোমে ফিরে যান" else "Return to Home"

    // Calendar
    fun progressCalendar(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অগ্রগতি ক্যালেন্ডার" else "Progress Calendar"
    fun calendarSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতিদিনের ধারাবাহিকতা পর্যবেক্ষণ করুন।" else "Track your daily consistency one day at a time."
    fun completedStatus(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সম্পন্ন" else "Completed"
    fun missedStatus(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ছুটি / মিসড" else "Rest / Missed"
    fun todayStatus(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আজ" else "Today"
    fun upcomingStatus(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আসন্ন" else "Upcoming"
    fun monthlySummary(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মাসিক সারাংশ" else "Monthly Summary"
    fun consistencyRate(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ধারাবাহিকতার হার" else "Consistency"

    // Settings
    fun settingsTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সেটিংস" else "Settings"
    fun settingsSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পছন্দসমূহ ও অ্যাপ বিবরণ" else "Preferences and application details"
    fun sectionPersonalization(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ব্যক্তিগতকরণ" else "Personalization"
    fun sectionLanguage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা (Language)" else "Language"
    fun sectionAppearance(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "থিম ও ডিসপ্লে" else "Display & Theme"
    fun sectionSecurity(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিরাপত্তা ও নোটিফিকেশন" else "Security & Alerts"
    fun sectionData(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "তথ্য ও গোপনীয়তা" else "Data & Privacy"
    fun sectionAbout(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পরিচিতি" else "About"

    fun yourName(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার নাম" else "Your Name"
    fun updateName(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নাম পরিবর্তন" else "Update Name"
    fun appearanceSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "১০টি প্রিমিয়াম থিম নির্বাচন করুন" else "Choose from 10 Premium Themes"
    fun selectThemeTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রিমিয়াম থিম নির্বাচন করুন (১০টি)" else "Select Premium Theme (10 Available)"
    fun activeGoalBadge(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সক্রিয়" else "Active"
    fun switchGoalTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সক্রিয় লক্ষ্য পরিবর্তন করুন" else "Switch Active Goal"
    fun appLockTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ঐচ্ছিক অ্যাপ লক" else "Optional App Lock"
    fun appLockSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডিভাইস লকের মাধ্যমে লক্ষ্য সুরক্ষিত রাখুন" else "Protect goal logs with device lock"
    fun reminderTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "দৈনিক চেক-ইন রিমাইন্ডার" else "Daily Check-in Reminder"
    fun reminderSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রাত ৮:০০ টায় প্রতিদিনের বার্তা" else "Gentle daily alert at 8:00 PM"
    fun privacyTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "গোপনীয়তা" else "Privacy"
    fun privacySubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "১০০% অফলাইন। কোনো ট্র্যাকিং বা টেলিমিতি নেই।" else "100% Offline. Zero telemetry & no tracking."
    fun dataManagementTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডেটা ব্যবস্থাপনা" else "Data Management"
    fun dataManagementSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অফলাইন ডেটা রিসেট বা পরিষ্কার করুন" else "Reset or clear offline data"
    fun aboutNivo(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিভো সম্পর্কে" else "About NIVO"
    fun aboutDeveloper(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডেভেলপার পরিচিতি" else "About Developer"

    // Developer Screen
    fun devName(lang: AppLanguage) = "MD. MOJAHIDUR RAHMAN SAJID"
    fun devDepartmentLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিভাগ" else "Department"
    fun devDepartmentValue(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কম্পিউটার সায়েন্স অ্যান্ড টেকনোলজি" else "Computer Science & Technology"
    fun devInstitutionLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতিষ্ঠান" else "Institution"
    fun devInstitutionValue(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বগুড়া পলিটেকনিক ইনস্টিটিউট" else "Bogura Polytechnic Institute"
    fun devLocationLabel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অবস্থান" else "Location"
    fun devLocationValue(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বগুড়া, বাংলাদেশ" else "Bogura, Bangladesh"
    fun devPurposeTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিভো তৈরির উদ্দেশ্য" else "The Purpose of NIVO"
    fun devPurposeText(lang: AppLanguage): String {
        return if (lang == AppLanguage.BANGLA) {
            "নিভো তৈরি করা হয়েছে মানুষকে তাদের লক্ষ্যের প্রতি অবিচল থাকতে, ধীরে ধীরে শৃঙ্খলা গড়ে তুলতে এবং প্রতিদিন এক ধাপ করে এগিয়ে যাওয়ার জন্য সাহায্য করতে।\n\n" +
            "আসল সাফল্য হঠাৎ কোনো আবেগে আসে না; এটি আসে প্রতিদিনের বিনম্র ও অবিচল প্রতিশ্রুতির মাধ্যমে। সব ধরনের বিভ্রান্তি, বিজ্ঞাপন ও অনলাইন কোলাহল দূর করে নিভো একটি শান্ত, সম্পূর্ণ ব্যক্তিগত পরিবেশ প্রদান করে যেখানে আপনার পূর্ণ মনোযোগ থাকে কেবল আজকের দিনের দায়িত্বে।"
        } else {
            "NIVO was created to help people stay consistent with their goals, build discipline gradually, and focus on one day at a time.\n\n" +
            "Real growth rarely happens through sudden bursts of motivation; it emerges from modest, unwavering daily commitments. By removing all unnecessary noise, intrusive tracking, and online distractions, NIVO offers a calm, private sanctuary where your daily focus remains purely on the present day's progress."
        }
    }

    // Dialogs
    fun selectLanguageTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা নির্বাচন করুন" else "Select Language"
    fun deleteGoalConfirmTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লক্ষ্য মুছে ফেলবেন?" else "Delete Goal?"
    fun deleteGoalConfirmMessage(lang: AppLanguage, title: String) = if (lang == AppLanguage.BANGLA) "আপনি কি নিশ্চিতভাবে \"$title\" মুছে ফেলতে চান? এই কাজটি পুনরায় ফিরিয়ে আনা যাবে না।" else "Are you sure you want to delete \"$title\"? This action cannot be undone."
    fun resetConfirmTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাপের সমস্ত ডেটা রিসেট করবেন?" else "Reset Application Data?"
    fun resetConfirmMessage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এটি আপনার সংরক্ষিত সকল লক্ষ্য এবং চেক-ইনের ইতিহাস মুছে ফেলবে। আপনি কি নিশ্চিত?" else "This will clear all created goals and check-in history from local storage. Are you sure?"
    fun doneButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সম্পন্ন" else "Done"

    // PIN Lock
    fun enterPinTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পিন কোড লিখুন" else "Enter PIN"
    fun enterPinSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিভো আনলক করতে আপনার ৪-সংখ্যার পিন দিন" else "Enter your 4-digit PIN to unlock NIVO"
    fun setPinTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন পিন কোড সেট করুন" else "Set New PIN"
    fun setPinSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "যেকোনো ৪-সংখ্যার পিন নির্বাচন করুন" else "Choose any 4-digit PIN"
    fun confirmPinTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পিন নিশ্চিত করুন" else "Confirm PIN"
    fun confirmPinSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিশ্চিত করতে একই পিন পুনরায় লিখুন" else "Re-enter the same 4-digit PIN to confirm"
    fun disablePinTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বর্তমান পিন দিন" else "Enter Current PIN"
    fun disablePinSubtitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাপ লক বন্ধ করতে বর্তমান পিন দিন" else "Enter current PIN to disable App Lock"
    fun errorPinMismatch(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পিন মেলেনি, আবার চেষ্টা করুন" else "PIN does not match, please try again"
    fun errorIncorrectPin(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভুল পিন কোড" else "Incorrect PIN"
    fun pinLockEnabledSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাপ লক সক্রিয় করা হয়েছে" else "App Lock has been enabled"
    fun pinLockDisabledSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাপ লক নিষ্ক্রিয় করা হয়েছে" else "App Lock has been disabled"
    fun changePin(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পিন পরিবর্তন করুন" else "Change PIN"

    // Backup & Export
    fun exportDataTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডেটা এক্সপোর্ট (ব্যাকআপ)" else "Export Data (Backup)"
    fun exportDataDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার অফলাইন লক্ষ্য এবং রেকর্ডের ব্যাকআপ টেক্সট কপি করুন" else "Copy your offline goals and check-in records as backup text"
    fun copyToClipboard(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্লিপবোর্ডে কপি করুন" else "Copy to Clipboard"
    fun copiedSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কপি করা হয়েছে!" else "Copied to clipboard!"
}
