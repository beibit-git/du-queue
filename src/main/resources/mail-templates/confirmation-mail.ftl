<#assign subject = "Email Confirmation">

<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
        }
        .container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            margin: 20px 0;
            font-size: 16px;
            color: #ffffff;
            background-color: #007bff;
            text-align: center;
            text-decoration: none;
            border-radius: 5px;
        }
        .footer {
            margin-top: 20px;
            font-size: 12px;
            color: #999999;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Подтверждение электронной почты</h2>
    <p>Уважаемый(ая) ${name},</p>
    <p>Спасибо за регистрацию на нашей платформе. Пожалуйста, подтвердите свой адрес электронной почты, нажав на кнопку ниже:</p>
    <a href="${confirmationLink}" class="button">Подтвердить электронную почту</a>
    <p>Если вы не создавали учетную запись, никаких действий не требуется.</p>
    <p class="footer">С уважением,<br/> Университет Дулати</p>
</div>
</body>
</html>
