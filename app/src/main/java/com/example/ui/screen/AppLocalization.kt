package com.example.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

// =============================================================
// APP LOCALIZATION SYSTEM
// Supports: Arabic (ar) | English (en)
// Usage anywhere: "نص عربي".loc()
// =============================================================

/** Global language state — updated when user changes language */
object AppLang {
    var current: String = "ar"
}

val LocalLanguage = compositionLocalOf { "ar" }

/** Full map of Arabic ↔ English translations */
val translations: Map<String, String> = mapOf(
    // ── App Title & Subtitle ──────────────────────────────────
    "تيجان النور 📖" to "Nour Crowns 📖",
    "تيجان النور" to "Nour Crowns",
    "متابعة تلاوة وحفظ القرآن الكريم للطلاب والناشئة" to "Quran Memorization Tracking for Students",
    "نظام ريادي متكامل لمتابعة وتدوين مستوى تلاوة وحفظ القرآن الكريم للطلاب والناشئة، يدعم الرصد الأسبوعي والتقارير الدورية ومشاركة النتائج مع أولياء الأمور." to
            "A comprehensive system for tracking Quran memorization and recitation progress for students, supporting weekly monitoring, periodic reports, and sharing results with parents.",
    "الإصدار الحالي: v5.0.0\nمطور بكل حب ومودة لخدمة كتاب الله عز وجل 🌱" to
            "Current version: v5.0.0\nDeveloped with love and dedication to serve the Holy Quran 🌱",

    // ── Navigation / Actions ──────────────────────────────────
    "رجوع" to "Back",
    "إلغاء" to "Cancel",
    "حفظ" to "Save",
    "إضافة" to "Add",
    "تعديل" to "Edit",
    "حذف" to "Delete",
    "إغلاق" to "Close",
    "تأكيد" to "Confirm",
    "نعم" to "Yes",
    "لا" to "No",
    "بحث" to "Search",
    "تخطي" to "Skip",
    "نسخ" to "Copy",

    // ── Students List Screen ──────────────────────────────────
    "إجمالي الطلاب" to "Total Students",
    "حلقات النشاط" to "Active Circles",
    "بحث عن طالب أو حلقة..." to "Search for a student or circle...",
    "مسح البحث" to "Clear search",
    "لا يوجد طلاب مسجلين لعرض التقارير!" to "No students registered to show reports!",
    "لا يوجد طلاب مسجلين بعد" to "No students registered yet",
    "لم يتم العثور على نتائج للمطابقة" to "No matching results found",
    "اضغط على زر الإضافة (+) بالأعلى للبدء" to "Press the Add (+) button above to start",
    "تأكد من كتابة الاسم بشكل صحيح" to "Check that the name is spelled correctly",
    "إضافة طالب" to "Add Student",
    "قالَ رَسُولُ اللَّهِ ﷺ:" to "The Prophet ﷺ said:",
    "«خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ»" to "«The best among you are those who learn the Quran and teach it»",
    "غير محدد" to "Not set",
    "غير محددة" to "Not set",

    // ── Student Card ──────────────────────────────────────────
    "المعلم: " to "Teacher: ",
    "حذف الطالب" to "Delete Student",
    "حذف ملف الطالب؟" to "Delete Student File?",
    "هل أنت متأكد من حذف الطالب" to "Are you sure you want to delete student",
    "وكامل سجلات متابعته بشكل نهائي؟ لا يمكن التراجع عن هذا الإجراء." to "and all their tracking records permanently? This action cannot be undone.",
    "حذف نهائي" to "Delete Permanently",

    // ── Add/Edit Student Dialog ───────────────────────────────
    "إضافة طالب جديد للحلقة ✨" to "Add New Student ✨",
    "تعديل بيانات الطالب ✏️" to "Edit Student Data ✏️",
    "تعديل بيانات الطالب 📝" to "Edit Student Data ✏️",
    "اسم الطالب الكامل *" to "Full Student Name *",
    "الصف / حلقة التحفيظ" to "Class / Memorization Circle",
    "اسم المعلم المربي" to "Teacher Name",
    "رقم الواتساب (اختياري، مثلاً +966501234567)" to "WhatsApp Number (optional, e.g. +1234567890)",
    "مواعيد حلقة الطالب (اختر الأيام والأوقات) ⏰" to "Student Circle Schedule (select days & times) ⏰",
    "ملحوظة" to "Note",
    "اسم الطالب مطلوب للرصد" to "Student name is required",
    "إضافة وحفظ" to "Add & Save",
    "حفظ التعديلات" to "Save Changes",
    "السبت" to "Saturday",
    "الأحد" to "Sunday",
    "الاثنين" to "Monday",
    "الثلاثاء" to "Tuesday",
    "الأربعاء" to "Wednesday",
    "الخميس" to "Thursday",
    "الجمعة" to "Friday",

    // ── Student Profile Screen ────────────────────────────────
    "ملف الطالب ومتابعته" to "Student File & Tracking",
    "تعديل البيانات" to "Edit Data",
    "إضافة أسبوع رصد" to "Add Tracking Week",
    "🏫 الحلقة:" to "🏫 Circle:",
    "👤 المعلم:" to "👤 Teacher:",
    "📝 ملحوظة:" to "📝 Note:",
    "📊 تقرير فترة زمنية" to "📊 Period Report",
    "جداول ورصد المتابعة الأسبوعية 📅" to "Weekly Tracking Records 📅",
    "لا توجد أسابيع رصد مسجلة للطالب" to "No tracking weeks registered for this student",
    "انقر على زر 'إضافة أسبوع رصد' بالأسفل لبدء رصد الجداول" to "Press 'Add Tracking Week' button below to start tracking",
    "إضافة أسبوع متابعة جديد" to "Add New Tracking Week",
    "اكتب عنوان الأسبوع أو الفترة (مثال: الأسبوع الأول - محرم، أو رصد يونيو):" to "Enter week title or period (e.g. First Week - Muharram, or June tracking):",
    "عنوان أسبوع الرصد" to "Tracking Week Title",
    "إنشاء السجل" to "Create Record",
    "الأسبوع " to "Week ",

    // ── Weekly Report Card ────────────────────────────────────
    "رسالة التشجيع: " to "Encouragement: ",
    "اضغط للبدء في تدوين وحفظ رصد الأيام" to "Press to start recording daily tracking",
    "تعديل اسم الأسبوع 📝" to "Edit Week Name 📝",
    "اسم أسبوع الرصد" to "Tracking Week Name",
    "حذف الأسبوع؟" to "Delete Week?",
    "هل أنت متأكد من حذف" to "Are you sure you want to delete",
    "مع كافة عمليات الحفظ المسجلة فيه؟" to "and all recorded sessions within it?",
    "تعديل اسم الأسبوع" to "Edit Week Name",

    // ── Report Tracking Screen ────────────────────────────────
    "💡 معلومات الاستخدام:" to "💡 Usage Info:",
    "قم بإدخال السورة والآيات لكل يوم. اضغط على شارة التقييم (مثيل: الممتاز) لتبديل التقييم بالنجوم بكل سلاسة (⭐⭐⭐ -> ⭐⭐ -> ⭐ -> ❌ -> لم يرصد)." to
            "Enter the Surah and verses for each day. Press the rating badge to cycle through ratings (⭐⭐⭐ -> ⭐⭐ -> ⭐ -> ❌ -> not recorded).",
    "📖 لا توجد أيام مسجلة في هذا الأسبوع حتى الآن" to "📖 No days recorded in this week yet",
    "ابدأ بإضافة يوم جديد لتسجيل حفظ ومراجعة الطالب." to "Start by adding a new day to record memorization and revision.",
    "إضافة يوم جديد للأسبوع" to "Add New Day to Week",
    "مشاركة التقرير مع الوالدين" to "Share report with parents",
    "مشاركة التقرير كملف PDF 📄" to "Share Report as PDF 📄",
    "📝 ملاحظة لليوم (اختياري)" to "📝 Day Note (optional)",
    "يمكنك إضافة ملاحظة أو تعليق على هذا اليوم (ستظهر في سجل اليوم ويمكن تعديلها لاحقاً)." to
            "You can add a note for this day (it will appear in the day record and can be edited later).",
    "مثال: غاب الطالب عن الحلقة بعذر..." to "Example: Student was absent from class...",
    "إضافة اليوم" to "Add Day",
    "🌟 ملاحظة غراس المربي وتشجيعه للأسبوع:" to "🌟 Teacher's Note & Weekly Encouragement:",
    "تظهر هذه الرسالة في التقرير المشترك مع الوالدين لتحفيز البطل بكلمات طيبة." to
            "This message appears in the shared report with parents to motivate the student.",
    "اكتب رسالة تشجيعية هنا (مثال: واصل تميزك يا بطل، فخورين بك!)" to
            "Write an encouraging message here (e.g. Keep up your excellence, champion!)",

    // ── Day Record Box ────────────────────────────────────────
    "يوم " to "Day ",
    "اختر التاريخ 📅" to "Select date 📅",
    "اختر التاريخ" to "Select date",
    "حذف اليوم" to "Delete Day",
    "حذف اليوم؟" to "Delete Day?",
    "هل أنت متأكد من حذف يوم" to "Are you sure you want to delete day",
    "وتاريخه وكل بيانات الحفظ والمراجعة المسجلة فيه؟" to "and its date and all recorded memorization data?",
    "نعم، احذف" to "Yes, Delete",
    "🌱 الْحِفْظُ الْجَدِيدُ" to "🌱 New Memorization",
    "💧 الْمَاضِي الْقَرِيبُ" to "💧 Recent Revision",
    "🔥 الْمَاضِي الْبَعِيدُ" to "🔥 Distant Revision",
    "من سورة" to "From Surah",
    "إلى سورة" to "To Surah",
    "الآية" to "Verse",

    // ── Rating Labels ─────────────────────────────────────────
    "ممتاز ⭐⭐⭐" to "Excellent ⭐⭐⭐",
    "جيد ⭐⭐" to "Good ⭐⭐",
    "مقبول ⭐" to "Acceptable ⭐",
    "ضعيف ❌" to "Weak ❌",
    "لم يرصد" to "Not Recorded",
    "ممتاز" to "Excellent",
    "جيد" to "Good",
    "مقبول" to "Acceptable",
    "ضعيف" to "Weak",

    // ── Period Report Screen ──────────────────────────────────
    "تقرير فترة زمنية" to "Period Report",
    "تقرير أداء الطالب" to "Student Performance Report",
    "اختر تاريخ البداية" to "Select start date",
    "اختر تاريخ النهاية" to "Select end date",
    "من:" to "From:",
    "إلى:" to "To:",
    "عرض التقرير" to "View Report",
    "مشاركة التقرير" to "Share Report",
    "لا توجد بيانات في الفترة المحددة" to "No data in the selected period",
    "تاريخ البداية" to "Start Date",
    "تاريخ النهاية" to "End Date",
    "عرض التقرير 📊" to "View Report 📊",
    "تصدير PDF 📄" to "Export PDF 📄",
    "لم يُحدد تاريخ" to "No date set",

    // ── Backup Screen ─────────────────────────────────────────
    "النسخ الاحتياطي واستيراد البيانات" to "Backup & Import Data",
    "تصدير البيانات" to "Export Data",
    "استيراد البيانات" to "Import Data",
    "نسخ احتياطي" to "Backup",
    "استرجاع البيانات 💾" to "Restore Data 💾",
    "صفحة النسخ الاحتياطي واسترجاع البيانات 💾" to "Backup & Restore Page 💾",
    "تصدير نسخة احتياطية 📤" to "Export Backup 📤",
    "استيراد نسخة احتياطية 📥" to "Import Backup 📥",
    "تم التصدير بنجاح" to "Exported successfully",
    "تم الاستيراد بنجاح" to "Imported successfully",

    // ── Settings Screen ───────────────────────────────────────
    "إعدادات التطبيق ⚙️" to "App Settings ⚙️",
    "🎨 المظهر وطابع التطبيق:" to "🎨 Appearance & Theme:",
    "الوضع الداكن (Dark Mode)" to "Dark Mode",
    "🌐 اللغة / Language:" to "🌐 Language / اللغة:",
    "عربي" to "Arabic",
    "✓ محدد" to "✓ Selected",
    "* سيتم إعادة تشغيل التطبيق تلقائياً عند تغيير اللغة" to "* App will restart automatically when language is changed",
    "🔔 تنبيهات وإشعارات المتابعة:" to "🔔 Tracking Notifications & Alerts:",
    "تفعيل الإشعارات العامة" to "Enable General Notifications",
    "وقت التنبيه قبل الحلقة (بالدقائق)" to "Alert time before class (in minutes)",
    "وقت إشعار التذكير اليومي" to "Daily reminder notification time",
    "تخصيص الإشعارات حسب الحلقات النشطة:" to "Customize notifications by active circles:",
    "لا توجد حلقات مسجلة حالياً." to "No circles registered currently.",
    "حلقة: " to "Circle: ",
    "تنبيه الحلقات ⏰" to "Circle Alerts ⏰",
    "إشعارات التقارير 📋" to "Report Notifications 📋",
    "تنبيهات إضافية أخرى 🔔" to "Other Alerts 🔔",
    "🛠️ إجراءات سريعة وصيانة:" to "🛠️ Quick Actions & Maintenance:",
    "إرسال إشعار تجريبي فوراً 🔔" to "Send Test Notification 🔔",
    "ℹ️ حول التطبيق:" to "ℹ️ About the App:",

    // ── Group Report Dialog ───────────────────────────────────
    "📊 تقرير طلاب الحلقة" to "📊 Circle Students Report",
    "اختر الحلقة" to "Select Circle",
    "جميع الحلقات" to "All Circles",
    "لا يوجد طلاب في هذه الحلقة حالياً." to "No students in this circle currently.",
    "نسخ الجدول 📋" to "Copy Table 📋",
    "تصدير PDF 📄" to "Export PDF 📄",
    "تم نسخ الجدول للحافظة بنجاح!" to "Table copied to clipboard successfully!",
    "إغلاق" to "Close",

    // ── Shared PDF/Report headers ─────────────────────────────
    "الاسم" to "Name",
    "الحلقة" to "Circle",
    "الوقت" to "Time",
    "الواتساب" to "WhatsApp",
    "الرقم التسلسلي" to "Seq. No.",
    "اسم الطالب الكامل" to "Full Student Name",
    "رقم التواصل (الواتساب)" to "Contact (WhatsApp)",
    "مواعيد وأوقات الحلقة" to "Circle Schedule",
    "🏫 اسم الحلقة:" to "🏫 Circle Name:",
    "👥 عدد الطلاب:" to "👥 Student Count:",
    "📅 تاريخ التصدير:" to "📅 Export Date:",
    " طالب" to " students",

    // ── Notifications ─────────────────────────────────────────
    "تنبيه تجريبي 🔔" to "Test Notification 🔔",
    "مرحباً بك! هذا تنبيه تجريبي من تطبيق تيجان النور للتأكد من عمل نظام الإشعارات." to
            "Welcome! This is a test notification from Nour Crowns to confirm the notification system is working.",

    // ── Splash Screen ─────────────────────────────────────────
    "جارٍ التحميل..." to "Loading...",
    "نظام متابعة حفظ القرآن الكريم" to "Quran Memorization Tracking System",

    // ── Additional UI, Backup & PDF Translations ───────────────
    "💾 النسخ الاحتياطية" to "Backups 💾",
    "⚡ إجراءات سريعة:" to "⚡ Quick Actions:",
    "جاري التصدير..." to "Exporting...",
    "📤 تصدير نسخة" to "📤 Export Backup",
    "📥 استيراد نسخة" to "📥 Import Backup",
    "تم التصدير بنجاح!" to "Exported successfully!",
    "فشل التصدير" to "Export failed",
    "خطأ غير معروف" to "Unknown error",
    "تم الاستيراد بنجاح!" to "Imported successfully!",
    "فشل الاستيراد" to "Import failed",
    "النسخة بتاريخ: " to "Backup date: ",
    "غير معروف" to "Unknown",
    "⚙️ إعدادات النسخ التلقائي:" to "⚙️ Auto-Backup Settings:",
    "النسخ التلقائي اليومي" to "Daily Auto-Backup",
    "إنشاء نسخة احتياطية تلقائياً كل يوم" to "Create backup automatically every day",
    "⏰ وقت النسخ" to "⏰ Backup Time",
    "📦 الحد الأقصى للنسخ" to "📦 Max Backups",
    "زيادة" to "Increase",
    "آخر نسخة تلقائية: " to "Last auto-backup: ",
    "نجح" to "Success",
    "فشل" to "Failed",
    "📁 النسخ المحفوظة " to "Saved Backups ",
    "لا توجد نسخ احتياطية محفوظة بعد" to "No saved backups yet",
    "اضغط 'تصدير نسخة' لإنشاء أول نسخة احتياطية" to "Press 'Export Backup' to create your first backup",
    "استعادة" to "Restore",
    "حذف" to "Delete",
    "📋 سجل العمليات الأخيرة:" to "📋 Recent Activity Log:",
    "تأكيد الاستيراد" to "Confirm Import",
    "⚠️ تنبيه مهم: عملية الاستيراد ستحذف جميع البيانات الحالية وتستبدلها ببيانات النسخة الاحتياطية!" to "⚠️ Important Alert: Importing will overwrite all current data with backup data!",
    "📊 فحص الملف:" to "📊 File Check:",
    "محتوى النسخة: " to "Backup content: ",
    " طالب | " to " student(s) | ",
    " تقرير | " to " report(s) | ",
    " سجل" to " record(s)",
    "⚠️ تحذيرات: " to "⚠️ Warnings: ",
    "❌ أخطاء: " to "❌ Errors: ",
    "استيراد واستبدال البيانات" to "Import & Replace Data",
    "حذف النسخة الاحتياطية؟" to "Delete Backup?",
    "هل أنت متأكد من حذف النسخة \"" to "Are you sure you want to delete backup \"",
    "\"؟ لا يمكن التراجع عن هذا الإجراء." to "\"? This action cannot be undone.",
    "نسخة تلقائية" to "Auto Backup",
    "نسخة يدوية" to "Manual Backup",
    "استيراد نسخة" to "Imported Backup",
    "الملف قابل للقراءة" to "File is readable",
    "صيغة JSON صحيحة" to "Valid JSON format",
    "بيانات وصفية موجودة" to "Metadata exists",
    "إصدار متوافق" to "Compatible version",
    "جميع الجداول موجودة" to "All tables exist",
    "سلامة البيانات" to "Data integrity",
    "العلاقات صحيحة" to "Valid relations",
    "🤖 تلقائي" to "🤖 Auto",
    "👤 يدوي" to "👤 Manual",
    "❌ لا يمكن قراءة الملف المحدد" to "❌ Cannot read the selected file",
    "الملف فارغ أو لا يمكن قراءته" to "The file is empty or cannot be read",
    "الملف ليس بصيغة JSON صحيحة" to "The file is not in valid JSON format",
    "فشل تحليل محتوى الملف" to "Failed to parse file content",
    "الملف لا يحتوي على بيانات وصفية (metadata) صحيحة" to "The file does not contain valid metadata",
    "بعض بيانات الطلاب تالفة (أسماء فارغة)" to "Some student data is corrupt (empty names)",
    "بعض بيانات التقارير تالفة (أسماء أسابيع فارغة)" to "Some report data is corrupt (empty week names)",
    "فشل إنشاء الملف في مجلد التنزيلات" to "Failed to create file in Downloads folder",
    "فشل كتابة البيانات" to "Failed to write data",
    "✅ تم إنشاء نسخة احتياطية" to "✅ Backup created successfully",
    "❌ فشل إنشاء النسخة الاحتياطية" to "❌ Backup creation failed",
    "إشعارات تيجان النور العامة" to "Nour Crowns General Notifications",
    "إشعارات الحلقات، تحديث الطلاب، والتنبيهات اليومية" to "Circle notifications, student updates, and daily alerts",
    "إشعارات النسخ الاحتياطية التلقائية" to "Automatic backup notifications",
    "تيجان النور لِمُتَابَعَةِ تِلَاوَةِ وَحِفْظِ الْقُرْآنِ الْكَرِيمِ 📖" to "Nour Crowns Quran Memorization & Recitation Tracking System 📖",
    "👤 الطَّالِبُ/ـة:" to "👤 Student:",
    "🏫 الصَّفُّ/الْحَلَقَةُ:" to "🏫 Class/Circle:",
    "👤 الْمُعَلِّمُ الْمُرَبِّي:" to "👤 Teacher:",
    "📅 فَتْرَةُ الرَّصْدِ:" to "📅 Tracking Period:",
    "اليوم والتاريخ" to "Day & Date",
    "💌 تَوْجِيهُ وَمُلَاحَظَاتُ الْمُرَبِّي لِلأُسْبُوعِ:" to "💌 Teacher's Weekly Feedback & Notes:",
    "📖 «خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ» - تم التصدير كملف PDF رقمي عبر تطبيق تيجان النور 📖" to
        "📖 «The best among you are those who learn the Quran and teach it» - Exported as PDF via Nour Crowns App 📖",
    "تصدير ومشاركة تقرير PDF:" to "Export & Share PDF Report:",
    "السلام عليكم ورحمة الله وبركاته، نرسل لكم تقرير مستوى تسميع ومتابعة القرآن الكريم لـ الأسبوع " to
        "Assalamu Alaikum, we send you the Quran recitation and memorization tracking report for week ",
    "للطالب البطل: " to "for the student: ",
    "نسأل الله أن يجعله من أهل القرآن." to "We pray to Allah to make them among the people of the Quran.",
    "تصدير ومشاركة تقرير الفترة:" to "Export & Share Period Report:",
    "تيجان النور: تقرير طلاب الحلقة 📊" to "Nour Crowns: Circle Students Report 📊",
    "صفحة " to "Page ",
    "🏫 اسم الحلقة:" to "🏫 Circle Name:",
    "👥 عدد الطلاب:" to "👥 Student Count:",
    " طالب" to " student(s)",
    "📅 تاريخ التصدير:" to "📅 Export Date:",
    "الرقم" to "No.",
    "اسم الطالب الكامل" to "Full Student Name",
    "اسم الحلقة" to "Circle Name",
    "الواتساب" to "WhatsApp",
    "مواعيد الحلقة" to "Circle Schedule",
    "الرقم التسلسلي" to "Seq. No.",
    "رقم التواصل (الواتساب)" to "Contact (WhatsApp)",
    "مواعيد وأوقات الحلقة" to "Circle Schedule",
    "تنبيه الحلقات" to "Circle Alerts",
    "إشعارات التقارير" to "Report Notifications",
    "تنبيهات إضافية أخرى" to "Other Alerts",
    "امتيـاز ⭐⭐⭐" to "Excellent ⭐⭐⭐",
    "امتياز ⭐⭐⭐" to "Excellent ⭐⭐⭐",
    "جيد جداً ⭐⭐" to "Very Good ⭐⭐",
    "جيد وطيب ⭐" to "Good ⭐",
    "إعادة تسميع ❌" to "Re-recite ❌",
    "لم يـرصد ⚪" to "Not Recorded ⚪",
    "لم يعقد" to "Not Recorded",
    "تعديل اسم الأسبوع" to "Edit Week Name",
    "تعديل اسم الأسبوع 📝" to "Edit Week Name 📝",
    "تنبيه تجريبي 🔔" to "Test Notification 🔔",
    "مرحباً بك! هذا تنبيه تجريبي من تطبيق تيجان النور للتأكد من عمل نظام الإشعارات." to
        "Welcome! This is a test notification from Nour Crowns to confirm the notification system is working.",
    "صيغة رقم الواتساب غير صحيحة! يجب أن يبدأ بـ + يليه رمز الدولة والأرقام (مثال: +966501234567)" to
        "Invalid WhatsApp format! Must start with + followed by country code and digits (e.g., +1234567890)",
    "رقم الواتساب هذا مسجل بالفعل لطالب آخر في نفس الحلقة!" to
        "This WhatsApp number is already registered for another student in the same circle!",
    "خطأ أثناء إضافة الطالب: " to "Error while adding student: ",
    "خطأ أثناء تحديث بيانات الطالب: " to "Error while updating student: ",
    "📝 أضف ملاحظة لليوم (اختياري)..." to "📝 Add a note for the day (optional)...",
    "تنبيه الحلقات ⏰" to "Circle Alerts ⏰",
    "إشعارات التقارير 📋" to "Report Notifications 📋",
    "تنبيهات إضافية أخرى 🔔" to "Other Alerts 🔔",
    "حلقة: " to "Circle: ",
    "تنبيهات وإشعارات المتابعة:" to "Tracking Notifications & Alerts:",
)

/**
 * Returns the localized version of this string.
 * Works anywhere — in Composables, click handlers, and callbacks.
 */
fun String.loc(): String {
    if (AppLang.current != "en") return this
    val direct = translations[this]
    if (direct != null) return direct

    // Dynamic patterns
    if (this.startsWith("فشل التصدير: ")) {
        return "Export failed: " + this.substringAfter("فشل التصدير: ")
    }
    if (this.startsWith("فشل الاستيراد: ")) {
        return "Import failed: " + this.substringAfter("فشل الاستيراد: ")
    }
    if (this.startsWith("الملف من إصدار أحدث (") && this.contains(") ولا يمكن استيراده بالإصدار الحالي (")) {
        val fileVersion = this.substringAfter("الملف من إصدار أحدث (").substringBefore(")")
        val currentVersion = this.substringAfter("ولا يمكن استيراده بالإصدار الحالي (").substringBefore(")")
        return "The file is from a newer version ($fileVersion) and cannot be imported in the current version ($currentVersion)"
    }
    if (this.startsWith("الملف من إصدار أقدم (") && this.endsWith("). قد تكون بعض البيانات غير متوافقة.")) {
        val fileVersion = this.substringAfter("الملف من إصدار أقدم (").substringBefore(")")
        return "The file is from an older version ($fileVersion). Some data might be incompatible."
    }

    val orphanReportsRegex = """(\d+) تقرير أسبوعي مرتبط بطالب غير موجود""".toRegex()
    val orphanReportsMatch = orphanReportsRegex.find(this)
    if (orphanReportsMatch != null) {
        val count = orphanReportsMatch.groupValues[1]
        return "$count weekly report(s) associated with a non-existent student"
    }

    val orphanLogsRegex = """(\d+) سجل يومي مرتبط بتقرير غير موجود""".toRegex()
    val orphanLogsMatch = orphanLogsRegex.find(this)
    if (orphanLogsMatch != null) {
        val count = orphanLogsMatch.groupValues[1]
        return "$count daily record(s) associated with a non-existent report"
    }

    return this
}
