<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Vstreem verification</title>
    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />

    <!-- Bootstrap CSS -->
    <link
      rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
      integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
      crossorigin="anonymous"
    />
    <!-- Foont Fmilt Link -->
    <link rel="preconnect" href="https://fonts.gstatic.com" />
    <link
      href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,400;0,500;0,600;0,700;0,800;0,900;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
      rel="stylesheet"
    />
    <!-- Boxicons CSS -->
    <link
      href="https://unpkg.com/boxicons@2.0.7/css/boxicons.min.css"
      rel="stylesheet"
    />

    <!-- Boxicons JS -->
    <link
      href="https://unpkg.com/boxicons@2.0.7/dist/boxicons.js"
      rel="stylesheet"
    />
  </head>
  <body style="color: #001028; font-family: Poppins">
    <div style="background-color: #fff">
      <div
        style="
          padding-bottom: 3rem;
          background-color: rgba(255, 186, 0, 0.05);
          padding-left: 2rem;
          padding-right: 2rem;
        "
      >
        <div class="verification-logo">
          <img
            class=""
            src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/vestreem_logo.png"
            alt="vstreem"
            title="vstreem"
          />
        </div>
        <h2 style="margin-bottom: 1rem; margin-top: 0.5rem; font-size: 20px">
          Reset your password?
        </h2>
        <p
          style="
            font-weight: 400;
            color: rgba(0, 16, 40, 0.9);
            font-size: 20px;
            margin-bottom: 2rem;
          "
        >
          If you requested a password reset for
          <span style="font-weight: 600">${userMail}</span>, click the
          button below. If you didn’t make this request, Ignore this email.
        </p>
        <div>
          <a style="font-size: 16px" href="${regisrationLink}">
            <button
              style="
                background-color: #1478ff;
                color: #ffffff;
                font-size: 20px;
                font-weight: 400;
                text-align: center;
                border-radius: 32px;
                outline: none !important;
                border: none;
                margin-bottom: 1rem;
                padding: 0.5rem 2.5rem;
                cursor: pointer;
              "
            >
              Reset password
            </button>
          </a>
        </div>
        <hr
          style="
            border: 1px solid rgba(0, 16, 40, 0.2);
            width: 80%;
            margin-top: 3rem;
            margin-bottom: 3rem;
          "
        />
        <p
          style="
            font-weight: 400;
            color: rgba(0, 16, 40, 0.9);
            font-size: 14px;
            margin-bottom: 2rem;
          "
        >
          <span><b>"Choose a New Password”</b></span> button not working?
        </p>
        <div style="margin-bottom: 2rem">
          <a
            style="
              font-size: 16px;
              color: #1478ff;
              font-weight: 400;
              text-decoration: none !important;
            "
            href="${regisrationLink}"
            >${regisrationLink}</a
          >
        </div>
        <div>
          <p
            style="
              font-weight: 400;
              color: rgba(0, 16, 40, 0.9);
              font-size: 22px;
              margin-bottom: 1rem;
            "
          >
            Thanks,
          </p>
          <p
            style="
              font-weight: 400;
              color: rgba(0, 16, 40, 0.9);
              font-size: 22px;
              margin-bottom: 2rem;
            "
          >
            Vstreem Team
          </p>
        </div>
        <hr style="border: 1px solid rgba(0, 16, 40, 0.2); width: 50%" />
        <div style="text-align: center; margin-bottom: 3rem">
          <h3 style="margin-bottom: 3rem; margin-top: 3rem; text-align: center">
            Follow Us
          </h3>
          <div style="display: flex; justify-content: center;
          width: 20%;margin-left: auto;margin-right: auto;">
            <div
              style="
                width: 30px;
                height: 30px;
                margin-left: 0.5rem;
                margin-right: 0.5rem;
              "
            >
              <a
                target="_blank"
                href="https://www.facebook.com/vstreemservices"
                class="facebook"
              >
                <img
                  style="width: 100%"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/social-media/facebook.png"
                  alt="facebook"
                />
              </a>
            </div>
            <div
              style="
                width: 30px;
                height: 30px;
                margin-left: 0.5rem;
                margin-right: 0.5rem;
              "
            >
              <a
                target="_blank"
                href="https://www.linkedin.com/company/vstreem"
                class="linkedin"
              >
                <img
                  style="width: 100%"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/social-media/linkedin.png"
                  alt="linkedin"
                />
              </a>
            </div>
            <div
              style="
                width: 30px;
                height: 30px;
                margin-left: 0.5rem;
                margin-right: 0.5rem;
              "
            >
              <a
                target="_blank"
                href="https://www.instagram.com/vstreemservices/"
                class="instagram"
              >
                <img
                  style="width: 100%"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/social-media/instagram.png"
                  alt="instagram"
                />
              </a>
            </div>
            <div
              style="
                width: 30px;
                height: 30px;
                margin-left: 0.5rem;
                margin-right: 0.5rem;
              "
            >
              <a
                target="_blank"
                href="https://twitter.com/vstreemservices"
                class="twitter"
              >
                <img
                  style="width: 100%"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/social-media/twitter.png"
                  alt="twitter"
                />
              </a>
            </div>
            <div
              style="
                width: 30px;
                height: 30px;
                margin-left: 0.5rem;
                margin-right: 0.5rem;
              "
            >
              <a
                target="_blank"
                href="https://www.youtube.com/channel/UC4XED_p8294oseSWdvGlCkQ/featured"
                class="youtube"
              >
                <img
                  style="width: 100%"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/social-media/youtube.png"
                  alt="youtube"
                />
              </a>
            </div>
          </div>
          <div class="mb-5">
            <a
              style="
                font-size: 16px;
                color: #1478ff;
                font-weight: 400;
                text-decoration: none !important;
                text-align: center;
              "
              href="#"
              >To know more & understand our plans & features? Compare plan
              options →</a
            >
          </div>
          <p>
            This email is shared because you just registered with vstreem
            services. This is our confirmation email, but not a promotional
            message. You will only receive marketing emails from us if you
            confirm your email address in the email we sent to you immediately
            after registering. 
          </p>
          <p style="text-align: center; font-weight: 500;width: 90%;">
            ™️vstrEEm. All rights reserved.
          </p>
          <div
            style="display: flex; justify-content: center; padding: 0rem 0.5rem;
             width: 30%;margin-left: auto;margin-right: auto;">
            <div style="margin-left: 0.5rem; margin-right: 0.5rem">Terms</div>
            <div style="margin-left: 0.5rem; margin-right: 0.5rem">
              Privdivcy
            </div>
            <div style="margin-left: 0.5rem; margin-right: 0.5rem">
              Unsubscribe
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script
      src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
      integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
      crossorigin="anonymous"
    ></script>
    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
      integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
      crossorigin="anonymous"
    ></script>
    <script
      src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
      integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
      crossorigin="anonymous"
    ></script>
  </body>
</html>
