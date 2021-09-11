<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script  type = "text/javascript">
function validateForm()
{
	var fname = document.getElementById("Fname").value;
	var lname = document.getElementById("Lname").value;
	var email = document.getElementById("email").value;
	var validityfname = fname.match(/[a-zA-Z]/gm);
	var validitylname = lname.match(/([A-Za-z])/);
	var validityemail = email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/);
	
	
	if(validityfname==null)
		{
		   alert("Invalid first name, try again");
		   return false;
		}
	else
	{ 
		if(validitylname==null)
	    {
			alert("invalid last name, try again");
			   return false;
			}
		else
			{
			if(validityemail==null)
				{
				alert("Email doesnt comply to given rules, try again");
				   return false;
				}
			else
				{
				return true;
				}
			}
		
	}
	

}

</script>
<title>Edit Profile
</title>
</head>
<body>

<form action = "EditProfile" method ="post" enctype="multipart/form-data" onSubmit="return validateForm()" >


<label for= "Fname">
First Name
</label>
<input type = "text" id="Fname" name ="Fname">
<br>
<label for= "Lname">
Last Name
</label>
<input type = "text" id="Lname" name ="Lname">
<br>
<label for= "gender">
Select a gender
</label>
<select name = "gender" id="gender">
<option value ="female"> Female </option>
<option value ="male"> Male </option>
<option value ="undisclosed"> Undisclosed </option>
</select>

<br>

<label for = "email">
Email
</label>
<input type = "text" id="email" name ="email">
<br>
<label for = "profilepic">
Profile Picture
</label>
<input type = "file" name = "profilepic">


<button type = "submit">Submit form</button>

</form>

</body>
</html>