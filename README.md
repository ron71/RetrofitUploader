<h1>Retrofit Uploader</h1>
<h2>Libraries used :</h2>
<ul>
  <li>Retrofit2</li>
  <li>nbsp-MaterialFilePicker</li>
 </ul>

<h2>Follow the steps to configure after cloning:</h2>
<ol>
  <li>
    <h4>Add the dependencies, under dependencies section of app gradle</h4>
   
    implementation 'com.android.support:design:27.1.1'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'
    implementation 'com.nbsp:library:1.09'      
    
    
  </li>
  <li>
    <h4>Add the url of the maven file on the file picker library, under android section of app gradle </h4>
    
    repositories {
        maven {
            url "http://dl.bintray.com/lukaville/maven"
        }
    }
    
</li>
  <li>
  <h4>Change BASE_URL to your server ipAdress or URL in MainActivity.java</h4>
  
     public  static  final String BASE_URL = "http://10.0.2.2/"; 
    //Change it to your server ipAdress, here 10.0.2.2 is localhost hardcoded for AVD
    
   </li>
  <li><h4>Change @Post annotation to path to you file saving script of your backend in IUploaderAPI Interface</h4>

    @POST("upload/upload.php") //In my case its upload.php

</li>
</ol>

<h3>
  Here's my backend file saving script in PHP:
  upload.php:-
</h3>

```php
<?php
    if(isset($_FILES["uploaded_file"]["name"])){

        $name = $_FILES["uploaded_file"]["name"];
        $tmp_name = $_FILES["uploaded_file"]["tmp_name"];
        $error = $_FILES["uploaded_file"]["error"];

        if(!empty($name)){
            $location = './assets/';
            if(!is_dir($location))
                mkdir($location);
            if(move_uploaded_file($tmp_name, $location.$name))
                echo "Uploaded";
            else
                echo "PLEASE CHOOSE A FILE";
            
        }
    }
?>

```
