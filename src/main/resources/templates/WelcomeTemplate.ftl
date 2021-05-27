<!DOCTYPE html>
<html lang="en">
<<<<<<< HEAD
<head>
<title>welcome Vstreem</title>
<!-- Required meta tags -->
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
​
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous" />
<!-- Foont Fmilt Link -->
<link rel="preconnect" href="https://fonts.gstatic.com" />
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,400;0,500;0,600;0,700;0,800;0,900;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
	rel="stylesheet" />
<!-- Boxicons CSS -->
<link href="https://unpkg.com/boxicons@2.0.7/css/boxicons.min.css"
	rel="stylesheet" />
​
<!-- Boxicons JS -->
<link href="https://unpkg.com/boxicons@2.0.7/dist/boxicons.js"
	rel="stylesheet" />
<style>
.body {
	color: #001028;
	font-family: Poppins;
}

.welcome-sec {
	background-color: #e5e5e5;
	padding-left: 1.5rem;
	padding-right: 1.5rem;
}

p {
	font-size: 24px;
	font-weight: 400;
	color: rgba(0, 16, 40, 0.9);
}

h2 {
	font-size: 32px;
	font-weight: 600;
}

.welcome-body {
	padding-bottom: 10rem;
	background-color: #fff;
}

hr {
	border: 1px solid rgba(0, 16, 40, 0.2);
	width: 50%;
}

.welcome-para {
	padding-left: 11.5rem;
	padding-right: 11.5rem;
	margin-bottom: 3rem;
	text-align: center;
}

a {
	color: #1478ff;
	font-weight: 500;
	text-decoration: none !important;
}

.rightsidecontent {
	padding-top: 10rem;
	padding-right: 3rem;
}

.footer {
	text-align: center;
}

.footer p {
	padding-left: 10rem;
	padding-right: 10rem;
	font-size: 16px;
	font-weight: 400;
	margin-bottom: 3rem;
}

.footer .social-links a {
	font-size: 18px;
	display: inline-block;
	background: #000000;
	color: #fff;
	line-height: 1;
	padding: 8px 0;
	margin-right: 14px;
	border-radius: 50%;
	text-align: center;
	width: 36px;
	height: 36px;
	transition: 0.3s;
}
/* media querys */
/* // X-Large devices (large desktops, less than 1400px) */
@media ( max-width : 1399.98px) {
}
/* // Large devices (desktops, less than 1200px) */
@media ( max-width : 1199.98px) {
}
/* // Medium devices (tablets, less than 992px) */
@media ( max-width : 991.98px) {
	.welcome-para {
		padding-left: 0.5rem;
		padding-right: 0.5rem;
	}
	.rightsidecontent {
		padding-top: 2rem;
		padding-right: 0rem;
	}
	.footer p {
		padding-left: 3rem;
		padding-right: 3rem;
	}
}
/* // Small devices (landscape phones, less than 768px) */
@media ( max-width : 767.98px) {
}
/* // X-Small devices (portrait phones, less than 576px) */
@media ( max-width : 575.98px) {
	h2 {
		font-size: 13px;
		text-align: center;
	}
	p {
		font-size: 12px;
	}
	.welcome-para {
		padding-left: 0.5rem;
		padding-right: 0.5rem;
	}
	.rightsidecontent {
		padding-top: 1rem;
		padding-right: 0rem;
	}
	.footer p {
		padding-left: 1rem;
		padding-right: 1rem;
	}
}
</style>
</head>
<body>
	<section class="welcome-sec">
		<div class="welcome-body">
			<div class="py-5">
				<img class="d-block mx-auto"
					src="https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/vestreem_logo.png"
					alt="vstreem" title="vstreem" />
			</div>
			<div class="mb-4">
				<img class="d-block mx-auto w-100"
					src="https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/welcome.jpg"
					alt="welcome" />
			</div>
			<div class="text-center mt-2 mb-5">Dear ${userMail}</div>
			<<% if(${password} != null){ %> <p> The Temporary Password for login is <b> ${password}</b></p> <% }%>

			<h2 class="text-center mb-5">Thank you & Welcome to Vstreem</h2>
			​
			<p class="welcome-para">vstrEEm is your best choice to Easily
				stream live videos and telecast on multiple platforms to increase
				viewership and customer base. Go live with COMBISTREAM on YouTube,
				Facebook, Instagram and others — at the same time, from one account.
			</p>
			​
			<p class="welcome-para">vstrEEm comes with a power packed DATA
				BONDING solution which enables you for an Uninterrupted Streaming of
				events from any remote location. vstrEEm uses an innovative
				technology that combines several devices like DATA (3G/4G/LTE) +
				Wifi + Ethernet into a single, robust internet connection for
				telecasting uninterrupted, good quality live video</p>
			<div class="text-center">
				<a href="#" class="mb-4">Learn more</a>
				<hr />
			</div>
			<div class="container">
				<div class="row">
					<div class="col-xl-6 col-md-6">
						<div>
							<img class="d-block mx-auto w-100"
								src="https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/combistreem.png"
								alt="" />
						</div>
					</div>
					<div class="col-xl-6 col-md-6">
						<div class="rightsidecontent">
							<h2 class="mb-4">Combistreem</h2>
							<p>For streaming online at any scale, Delivering unmatched
								viewer experiences. Present and monetise your live videos across
								multiple channels at the same time using vstrEEm’s scalable,
								24/7 industry-leading live streaming software.</p>
						</div>
					</div>
				</div>
				<div class="row">
					<hr />
					<div class="col-xl-6 col-md-6 order-sm-1 order-2">
						<div class="rightsidecontent">
							<h2 class="mb-4">DataBond</h2>
							<p>MORE BANDWIDTH. STRONGER SIGNAL. Use data bonding to put
								your live streaming on auto-pilot. Backed by vstrEEm’s data
								bonding technology, you can stream flawless live videos to
								YouTube, Facebook Live, or other CDNs</p>
						</div>
					</div>
					<div class="col-xl-6 col-md-6 order-sm-2 order-1">
						<div>
							<img class="d-block mx-auto w-100"
								src="https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/databond.png"
								alt="" />
						</div>
					</div>
				</div>
				​
				<div class="row">
					<hr />
					<div class="col-xl-6 col-md-6">
						<div>
							<img class="d-block mx-auto w-100"
								src="https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/gallery.png"
								alt="" />
						</div>
					</div>
					<div class="col-xl-6 col-md-6">
						<div class="rightsidecontent">
							<h2 class="mb-4">Gallery</h2>
							<p>Telecast from your BROWSER - connecting people worldwide
								through uninterrupted, secure and real-time live streaming
								experiences.</p>
						</div>
					</div>
				</div>
				​
				<div class="row">
					<div class="col-xl-6 col-md-6 order-sm-1 order-2">
						<div class="rightsidecontent">
							<h2 class="mb-4">Catalogue</h2>
							<p>RECORD/EDIT & SCHEDULE your Video BROADCAST at ease. Sit
								back & Relax while your videos are being connected with your
								audience worldwide on various platforms, uninterrupted.</p>
						</div>
					</div>
					<div class="col-xl-6 col-md-6 order-sm-2 order-1">
						<div>
							<img class="d-block mx-auto w-100"
								src="https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/catalogue.png"
								alt="" />
						</div>
					</div>
					<hr />
				</div>
			</div>
			<hr />
			<div class="footer">
				<h3 class="my-5">Follow Us</h3>
				<div class="social-links mb-5">
					<a target="_blank" href="https://www.facebook.com/vstreemservices"
						class="facebook"> <i class="bx bxl-facebook"></i></a> <a
						target="_blank" href="https://www.linkedin.com/company/vstreem"
						class="linkedin"><i class="bx bxl-linkedin"></i></a> <a
						target="_blank" href="https://www.instagram.com/vstreemservices/"
						class="instagram"><i class="bx bxl-instagram"></i></a> <a
						target="_blank" href="https://twitter.com/vstreemservices"
						class="twitter"><i class="bx bxl-twitter"></i></a> <a
						target="_blank"
						href="https://www.youtube.com/channel/UC4XED_p8294oseSWdvGlCkQ/featured"
						class="youtube"><i class="bx bxl-youtube"></i></a>
				</div>
				<div class="mb-5">
					<a href="#">To know more & understand our plans & features?
						Compare plan options →</a>
				</div>
				<p>This email is shared because you just registered with vstreem
					services. This is our confirmation email, but not a promotional
					message. You will only receive marketing emails from us if you
					confirm your email address in the email we sent to you immediately
					after registering.</p>
				<p>™️vstrEEm. All rights reserved.</p>
				<div class="d-flex justify-content-center">
					<div class="mx-3">Terms</div>
					<div class="mx-3">Privdivcy</div>
					<div class="mx-3">Unsubscribe</div>
				</div>
			</div>
		</div>
	</section>
	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous"></script>
</body>
</html>
=======
  <head>
    <title>welcome Vstreem</title>
    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <!-- Bootstrap CSS -->

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
    <div
      style="
        background-color: #e5e5e5;
        padding-left: 1.5rem;
        padding-right: 1.5rem;
      "
    >
      <div
        class="welcome-body"
        style="padding-bottom: 10rem; background-color: #fff"
      >
        <div style="padding: 3rem 0rem">
          <img
            style="display: block; margin: auto; margin-bottom: 2rem"
            src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/vestreem_logo.png"
            alt="vstreem"
            title="vstreem"
          />
        </div>
        <div style="padding-bottom: 2.5rem">
          <img
            style="display: block; margin: auto; width: 100%"
            src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/welcome.jpg"
            alt="welcome"
          />
        </div>
        <div style="text-align: center; padding: 1.5rem 1.5rem">
          Dear ${userMail},
        </div>
        <h2
          style="
            font-size: 22px;
            font-weight: 600;
            text-align: center;
            margin-bottom: 2rem;
          "
        >
          Thank you & Welcome to Vstreem
        </h2>

        <p
          style="
            font-size: 13px;
            font-weight: 400;
            color: rgba(0, 16, 40, 0.9);
            padding-left: 0.5rem;
            padding-right: 0.5rem;
            margin-bottom: 3rem;
          "
        >
          vstrEEm is your best choice to Easily stream live videos and telecast
          on multiple platforms to increase viewership and customer base. Go
          live with COMBISTREAM on YouTube, Facebook, Instagram and others — at
          the same time, from one account.
        </p>

        <p
          style="
            font-size: 13px;
            font-weight: 400;
            color: rgba(0, 16, 40, 0.9);
            padding-left: 0.5rem;
            padding-right: 0.5rem;
            margin-bottom: 3rem;
          "
        >
          vstrEEm comes with a power packed DATA BONDING solution which enables
          you for an Uninterrupted Streaming of events from any remote location.
          vstrEEm uses an innovative technology that combines several devices
          like DATA (3G/4G/LTE) + Wifi + Ethernet into a single, robust internet
          connection for telecasting uninterrupted, good quality live video
        </p>
        <div style="text-align: center">
          <a
            style="
              color: #1478ff;
              font-weight: 500;
              text-decoration: none !important;
            "
            href="#"
            class="mb-4"
            >Learn more</a
          >
          <hr style="border: 1px solid rgba(0, 16, 40, 0.2); width: 50%" />
        </div>
        <div>
          <div style="display: flex; flex-direction: row; align-items: center">
            <div style="flex: 0 0 40%">
              <img
                style="display: block; margin: auto; width: 100%"
                src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/combistreem.png"
                alt="vstreem"
              />
            </div>
            <div>
              <span style="font-weight: bold; font-size: 22px">
                Combistreem</span
              >
              <p>
                For streaming online at any scale, Delivering unmatched viewer
                experiences. Present and monetise your live videos across
                multiple channels at the same time using vstrEEm’s scalable,
                24/7 industry-leading live streaming software.
              </p>
            </div>
          </div>

          <hr style="border: 1px solid rgba(0, 16, 40, 0.2); width: 50%" />
          <div style="display: flex; flex-direction: row; align-items: center">
            <div style="flex: 0 0 60%; padding-left: 0.5rem">
              <span
                style="
                  font-size: 22px;
                  font-weight: 600;
                  text-align: center;
                  margin-bottom: 2rem;
                "
              >
                DataBond</span
              >
              <p>
                MORE BANDWIDTH. STRONGER SIGNAL. Use data bonding to put
                your live streaming on auto-pilot. Backed by vstrEEm’s data
                bonding technology, you can stream flawless live videos to
                YouTube, Facebook Live, or other CDNs
              </p>
            </div>
            <div>
              <img
                style="display: block; margin: auto; width: 100%"
                src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/databond.png"
                alt="vstreem"
              />
            </div>
          </div>

          <hr style="border: 1px solid rgba(0, 16, 40, 0.2); width: 50%" />
          <div style="display: flex; flex-direction: row; align-items: center">
            <div style="flex: 0 0 40%">
              <div>
                <img
                  style="display: block; margin: auto; width: 100%"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/gallery.png"
                  alt="vstreem"
                />
              </div>
            </div>
            <div>
              <div>
                <span
                  style="
                    font-size: 22px;
                    font-weight: 600;
                    text-align: center;
                    margin-bottom: 2rem;
                  "
                >
                  Gallery</span
                >
                <p>
                  Telecast from your BROWSER - connecting people worldwide
                  through uninterrupted, secure and real-time live streaming
                  experiences.
                </p>
              </div>
            </div>
          </div>
          <hr style="border: 1px solid rgba(0, 16, 40, 0.2); width: 50%" />

          <div style="display: flex; flex-direction: row; align-items: center">
            <div style="flex: 0 0 60%; padding-left: 0.5rem">
              <div>
                <span
                  style="
                    font-size: 22px;
                    font-weight: 600;
                    text-align: center;
                    margin-bottom: 2rem;
                  "
                >
                  Catalogue</span
                >

                <p>
                  RECORD/EDIT & SCHEDULE your Video BROADCAST at ease. Sit back
                  & Relax while your videos are being connected with your
                  audience worldwide on various platforms, uninterrupted.
                </p>
              </div>
            </div>
            <div>
              <div>
                <img
                  style="display: block; margin: auto; width: 100%"
                  alt="catalogue"
                  src="https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/catalogue.png"
                />
              </div>
            </div>
          </div>
        </div>
        <hr style="border: 1px solid rgba(0, 16, 40, 0.2); width: 50%" />
        <div style="text-align: center; margin-bottom: 3rem">
          <h3 style="margin-top: 3rem; margin-bottom: 3rem">Follow Us</h3>
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
        </div>
        <div style="margin-bottom: 3rem; text-align: center">
          <a
            style="
              color: #1478ff;
              font-weight: 500;
              text-decoration: none !important;
            "
            href="#"
            >To know more & understand our plans & features? Compare plan
            options →</a
          >
        </div>
        <p
          style="
            padding-left: 0.5rem;
            padding-right: 0.5rem;
            font-size: 16px;
            font-weight: 400;
            margin-bottom: 3rem;
          "
        >
          This email is shared because you just registered with vstreem
          services. This is our confirmation email, but not a promotional
          message. You will only receive marketing emails from us if you confirm
          your email address in the email we sent to you immediately after
          registering. 
        </p>
        <p style="text-align: center; font-weight: 500;width:90%">
          ™️vstrEEm. All rights reserved.
        </p>
        <div
            style="display: flex; justify-content: center; padding: 0rem 0.5rem;
             width: 30%;margin-left: auto;margin-right: auto;">
          <div style="margin-left: 0.5rem; margin-right: 0.5rem">Terms</div>
          <div style="margin-left: 0.5rem; margin-right: 0.5rem">Privdivcy</div>
          <div style="margin-left: 0.5rem; margin-right: 0.5rem">
            Unsubscribe
          </div>
        </div>
      </div>
    </div>

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
  </body>
</html>
>>>>>>> f7f15f1d11ade4315f681b478fbf792023e88a0a