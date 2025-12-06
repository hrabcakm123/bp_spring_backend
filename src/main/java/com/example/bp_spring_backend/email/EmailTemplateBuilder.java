package com.example.bp_spring_backend.email;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildWelcomeText(
            String userName,
            String email,
            String password
    ) {
        return String.format("""
<html>
  <body style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f8; padding: 20px; color: #333;">
    <div style="max-width:650px; margin:0 auto; background-color:#fff; border-radius:10px; box-shadow:0 3px 10px rgba(0,0,0,0.1); padding:30px;">

      <div style="text-align:center; border-bottom:3px solid #a3c4f3; padding-bottom:15px; margin-bottom:20px;">
        <h1 style="color:#0069d9; margin:0; font-size:28px;">Oznámenie o vytvorení účtu</h1>
        <p style="font-size:16px; color:#555;"><b>Architektúra počítačov</b></p>
      </div>

      <p style="font-size:16px; line-height:1.5;">
        Vážený používateľ, <b style="color:#0069d9;">%s</b>,
      </p>

      <p style="font-size:16px; line-height:1.5;">
        informujeme Vás, že Vám bol <b>vytvorený účet</b>.
      </p>

      <h2 style="color:#0069d9; font-size:20px; margin-top:20px;">Prihlasovacie údaje</h2>
      <table style="width:100%%; border-collapse:collapse; margin-bottom:15px; font-size:15px; border:1px solid #a3c4f3; border-radius:8px; overflow:hidden;">
        <tr style="background-color:#f7faff;">
          <td style="padding:10px; width:35%%; border-bottom:1px solid #a3c4f3;"><b>E-mail:</b></td>
          <td style="padding:10px; border-bottom:1px solid #a3c4f3;">%s</td>
        </tr>
        <tr style="background-color:#ffffff;">
          <td style="padding:10px;"><b>Heslo:</b></td>
          <td style="padding:10px;">%s</td>
        </tr>
      </table>

      <div style="border-top:2px solid #e0e0e0; margin-top:25px; padding-top:10px; font-size:14px; color:#555;">
        <p style="margin:0;">S pozdravom, <b>Tím BP Spring Backend</b></p>
      </div>

    </div>
  </body>
</html>
""", userName, email, password);
    }

    public String buildUpdatedLoginInfo(
            String userName,
            String email,
            String password
    ) {
        return String.format("""
<html>
  <body style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6f8; padding: 20px; color: #333;">
    <div style="max-width:650px; margin:0 auto; background-color:#fff; border-radius:10px; box-shadow:0 3px 10px rgba(0,0,0,0.1); padding:30px;">

      <div style="text-align:center; border-bottom:3px solid #a3c4f3; padding-bottom:15px; margin-bottom:20px;">
        <h1 style="color:#0069d9; margin:0; font-size:28px;">Oznámenie o zmene prihlasovacích údajov</h1>
        <p style="font-size:16px; color:#555;"><b>Architektúra počítačov</b></p>
      </div>

      <p style="font-size:16px; line-height:1.5;">
        Vážený používateľ, <b style="color:#0069d9;">%s</b>,
      </p>

      <p style="font-size:16px; line-height:1.5;">
        informujeme Vás, že Vám boli <b>zmenené prihlasovacie údaje</b>.
      </p>

      <h2 style="color:#0069d9; font-size:20px; margin-top:20px;">Prihlasovacie údaje</h2>
      <table style="width:100%%; border-collapse:collapse; margin-bottom:15px; font-size:15px; border:1px solid #a3c4f3; border-radius:8px; overflow:hidden;">
        <tr style="background-color:#f7faff;">
          <td style="padding:10px; width:35%%; border-bottom:1px solid #a3c4f3;"><b>E-mail:</b></td>
          <td style="padding:10px; border-bottom:1px solid #a3c4f3;">%s</td>
        </tr>
        <tr style="background-color:#ffffff;">
          <td style="padding:10px;"><b>Heslo:</b></td>
          <td style="padding:10px;">%s</td>
        </tr>
      </table>

      <div style="border-top:2px solid #e0e0e0; margin-top:25px; padding-top:10px; font-size:14px; color:#555;">
        <p style="margin:0;">S pozdravom, <b>Tím BP Spring Backend</b></p>
      </div>

    </div>
  </body>
</html>
""", userName, email, password);
    }

    public String buildSubstitutionInfoEmail(
            String userName,
            String studentName,
            String studentAisId,
            String day,
            String time,
            String date,
            String instructorName
    ) {
        return String.format("""
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <style>
      @media only screen and (max-width: 600px) {
        body { padding: 10px !important; }
        .container { padding: 20px !important; }
        h1 { font-size: 22px !important; }
        h2 { font-size: 16px !important; }
        p, td { font-size: 14px !important; }
      }
    </style>
  </head>
  <body style="font-family: 'Segoe UI', Arial, sans-serif; background-color:#f4f6f8; padding:20px; color:#333; margin:0;">
    <div class="container" style="max-width:650px; margin:0 auto; background-color:#fff; border-radius:10px; box-shadow:0 3px 10px rgba(0,0,0,0.1); padding:30px;">

      <div style="text-align:center; border-bottom:3px solid #0069d9; padding-bottom:15px; margin-bottom:20px;">
        <h1 style="color:#0069d9; margin:0; font-size:28px;">Oznámenie o náhrade cvičenia</h1>
        <p style="font-size:16px; color:#555;"><b>Architektúra počítačov</b></p>
      </div>

      <p style="font-size:16px; line-height:1.5;">
        Vážený používateľ, <b style="color:#0069d9;">%s</b>,
      </p>
      <p style="font-size:16px; line-height:1.5;">
        informujeme Vás, že si nižšie uvedený študent <b>nahradil cvičenie</b>.
      </p>

      <h2 style="color:#0069d9; font-size:20px; margin-top:20px;">Údaje o študentovi</h2>
      <table style="width:100%%; border-collapse:collapse; margin-bottom:15px; font-size:15px; border:1px solid #a3c2f0; border-radius:8px; overflow:hidden;">
        <tr style="background-color:#f7faff;">
          <td style="padding:10px; width:35%%; border-bottom:1px solid #a3c2f0;"><b>Meno:</b></td>
          <td style="padding:10px; border-bottom:1px solid #a3c2f0;">%s</td>
        </tr>
        <tr style="background-color:#ffffff;">
          <td style="padding:10px; border-bottom:1px solid #a3c2f0;"><b>AIS ID:</b></td>
          <td style="padding:10px; border-bottom:1px solid #a3c2f0;">%s</td>
        </tr>
      </table>

      <h2 style="color:#0069d9; font-size:20px; margin-top:20px;">Informácie o nahradenom cvičení</h2>
      <table style="width:100%%; border-collapse:collapse; margin-bottom:15px; font-size:15px; border:1px solid #a3c2f0; border-radius:8px; overflow:hidden;">
        <tr style="background-color:#f7faff;">
          <td style="padding:10px; width:35%%; border-bottom:1px solid #a3c2f0;"><b>Deň a čas:</b></td>
          <td style="padding:10px; border-bottom:1px solid #a3c2f0;">%s %s</td>
        </tr>
        <tr style="background-color:#ffffff;">
          <td style="padding:10px;"><b>Dátum:</b></td>
          <td style="padding:10px;">%s</td>
        </tr>
      </table>

      <div style="margin-top:20px; font-size:16px;">
        <p><b>Zmenu vykonal:</b> <span style="color:#0069d9;">%s</span></p>
      </div>

      <div style="border-top:2px solid #e0e0e0; margin-top:25px; padding-top:10px; font-size:14px; color:#555;">
        <p style="margin:0;">S pozdravom, <b>Tím BP Spring Backend</b></p>
      </div>

    </div>
  </body>
</html>
""",
                userName,
                studentName,
                studentAisId,
                day,
                time,
                date,
                instructorName
        );
    }
}
