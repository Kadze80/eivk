$(document).ready(function () {
});

if (!$.browser.msie && !navigator.javaEnabled()) {
    setMessage("messages-response", "messages-wrapper-error", "Java support doesn't turn ON in your browser. Please turn On it or setup <a href=\"http://java.com/ru/download/\" target=\"blank\">Java</a> and come try again");
} else {
    insertApplet();
    blockScreen();
}

function insertApplet() {
    document.writeln('<applet width="1" height="1"');
    document.writeln(' codebase="./"');
    document.writeln(' code="kz.gamma.TumarCSP"');
    document.writeln(' archive="/web/eivk/eivk/lib/sign-applet.jar"');
    document.writeln(' type="application/x-java-applet"');
    document.writeln(' mayscript="true"');
    document.writeln(' id="KncaApplet" name="KncaApplet">');
    document.writeln('<param name="code" value="kz.gamma.TumarCSP">');
    document.writeln('<param name="archive" value="/web/eivk/eivk/lib/sign-applet.jar">');
    document.writeln('<param name="mayscript" value="true">');
    document.writeln('<param name="scriptable" value="true">');
    document.writeln('<param name="language" value="ru">');
    document.writeln('<param name="separate_jvm" value="true">');
    document.writeln('</applet>');
}

function AppletIsReady() {
    unBlockScreen();
    $("#appstatus").text("Applet is ready!");
}

function blockScreen() {
    $.blockUI({
        message: '<img src="js/loading.gif" /><br/>Wait for loading Java-applet...',
        css: {
            border: 'none',
            padding: '15px',
            backgroundColor: '#000',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .5,
            color: '#fff'
        }
    });
}

function unBlockScreen() {
    $.unblockUI();
}