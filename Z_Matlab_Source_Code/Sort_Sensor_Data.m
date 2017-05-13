clc;
clear;
format compact;
if true
   clear variables
   % Read the data from the csv file
   data = csvread('bicpcyclingbar01(40rpm).csv',2);
   lili = data(:,1);      % This would hold the time variable        
   Roll1 = data(:,2)*(180/pi); % This would hold the first Roll variable
   Pitch1 = data(:,3)*(180/pi); % This would hold the first Pitch variable
   Yaw1 = data(:,4)*(180/pi);% This would hold the first Yaw variable
   Acc1x = data(:,5) * 0.061; % This would hold the the first sensor x-axis acceleration variable
   Acc1y = data(:,6)* 0.061; % This would hold the first sensor y-axis acceleration variable
   Acc1z = data(:,7)* 0.061; % This would hold the first sensor z-axis acceleration variable
   Gyro1x = data(:,8)/100;% This would hold the first sensor x-axis angular acceleration variable
   Gyro1y = data(:,9)/100;% This would hold the first sensor y-axis angular acceleration variable
   Gyro1z = data(:,10)/100;% This would hold the first sensor z-axis angular acceleration variable
   Q1w = data(:,11)*(1.80/pi);% This would hold the first sensor w-axis quarternion variable
   Q1x = data(:,12)*(1.80/pi);%This would hold the first sensor x-axis quarternion variable
   Q1y = data(:,13)*(1.80/pi);%This would hold the first sensor y-axis quarternion variable
   Q1z = data(:,14)*(1.80/pi);%This would hold the first sensor z-axis quarternion variable
   Roll2 = data(:,15)*(180/pi);
   Pitch2 = data(:,16)*(180/pi);
   Yaw2 = data(:,17)*(180/pi);
   Acc2x = data(:,18)* 0.061;
   Acc2y = data(:,19)* 0.061;
   Acc2z = data(:,20)* 0.061;
   Gyro2x = data(:,21)/100;
   Gyro2y = data(:,22)/100;
   Gyro2z = data(:,23)/100;
   Q2w = data(:,24)*(1.80/pi);
   Q2x = data(:,25)*(1.80/pi);
   Q2y = data(:,26)*(1.80/pi);
   Q2z = data(:,27)*(1.80/pi);
   Roll3 = data(:,28)*(180/pi);
   Pitch3 = data(:,29)*(180/pi);
   Yaw3 = data(:,30)*(180/pi);
   Acc3x = data(:,31)* 0.061;
   Acc3y = data(:,32)* 0.061;
   Acc3z = data(:,33)* 0.061;
   Gyro3x = data(:,34)/100;
   Gyro3y = data(:,35)/100;
   Gyro3z = data(:,36)/100;
   Q3w = data(:,37)*(1.80/pi);
   Q3x = data(:,38)*(1.80/pi);
   Q3y = data(:,39)*(1.80/pi);
   Q3z = data(:,40)*(1.80/pi);
   TimeSensor = lili(1:1000);
   Time_Length1 = (TimeSensor(length(TimeSensor))  - TimeSensor(1)); % the length of the time data
   Period_1 = (Time_Length1/length(TimeSensor));  % The period 
   Sample_rate1 = (1/Period_1) * 1000000; %The sample rate
  %% Interpolate the Sensor Data to a fixed sample rate using interp1 function
   xq1 = TimeSensor(1):Period_1*1.33:TimeSensor(length(TimeSensor)); % The adjusted sample  frequency
   Time_Sensorint = interp1(TimeSensor,TimeSensor,xq1,'linear'); % The interpolated Time variable for the first sensor
   Roll1_int = interp1(lili,Roll1,xq1,'cubic');
   Pitch1_int = interp1(lili,Pitch1,xq1,'cubic');
   Yaw1_int = interp1(lili,Yaw1,xq1,'cubic');
   Acc1x_int = interp1(lili,Acc1x,xq1,'cubic');
   Acc1y_int = interp1(lili,Acc1y,xq1,'cubic');
   Acc1z_int = interp1(lili,Acc1z,xq1,'cubic');
   Gyro1x_int = interp1(lili,Gyro1x,xq1,'cubic');
   Gyro1y_int = interp1(lili,Gyro1y,xq1,'cubic');
   Gyro1z_int = interp1(lili,Gyro1z,xq1,'cubic');
   Q1w_int = interp1(lili,Q1w,xq1,'cubic');
   Q1x_int = interp1(lili,Q1x,xq1,'cubic');
   Q1y_int = interp1(lili,Q1y,xq1,'cubic');
   Q1z_int = interp1(lili,Q1z,xq1,'cubic');
   Roll2_int = interp1(lili,Roll2,xq1,'cubic');
   Pitch2_int = interp1(lili,Pitch2,xq1,'cubic');
   Yaw2_int = interp1(lili,Yaw2,xq1,'cubic');
   Acc2x_int = interp1(lili,Acc2x,xq1,'cubic');
   Acc2y_int = interp1(lili,Acc2y,xq1,'cubic');
   Acc2z_int = interp1(lili,Acc2z,xq1,'cubic');
   Gyro2x_int = interp1(lili,Gyro2x,xq1,'cubic');
   Gyro2y_int = interp1(lili,Gyro2y,xq1,'cubic');
   Gyro2z_int = interp1(lili,Gyro2z,xq1,'cubic');
   Q2w_int = interp1(lili,Q2w,xq1,'cubic');
   Q2x_int = interp1(lili,Q2x,xq1,'cubic');
   Q2y_int = interp1(lili,Q2y,xq1,'cubic');
   Q2z_int = interp1(lili,Q2z,xq1,'cubic');
   Roll3_int = interp1(lili,Roll3,xq1,'cubic');
   Pitch3_int = interp1(lili,Pitch3,xq1,'cubic');
   Yaw3_int = interp1(lili,Yaw3,xq1,'cubic');
   Acc3x_int = interp1(lili,Acc3x,xq1,'cubic');
   Acc3y_int = interp1(lili,Acc3y,xq1,'cubic');
   Acc3z_int = interp1(lili,Acc3z,xq1,'cubic');
   Gyro3x_int = interp1(lili,Gyro3x,xq1,'cubic');
   Gyro3y_int = interp1(lili,Gyro3y,xq1,'cubic');
   Gyro3z_int = interp1(lili,Gyro3z,xq1,'cubic');
   Q3w_int = interp1(lili,Q3w,xq1,'cubic');
   Q3x_int = interp1(lili,Q3x,xq1,'cubic');
   Q3y_int = interp1(lili,Q3y,xq1,'cubic');
   Q3z_int = interp1(lili,Q3z,xq1,'cubic');
   
   %This cell nectar contains all the sensor data that have been
   %synchronized together
   nectar=[Time_Sensorint.',Roll1_int.', Pitch1_int.',Yaw1_int.',Acc1x_int.',Acc1y_int.',Acc1z_int.',Gyro1x_int.',Gyro1y_int.',Gyro1z_int.',Q1w_int.',Q1x_int.',Q1y_int.',Q1z_int.',Roll2_int.',Pitch2_int.',Yaw2_int.',Acc2x_int.',Acc2y_int.',Acc2z_int.',Gyro2x_int.',Gyro2y_int.',Gyro2z_int.',Q2w_int.',Q2x_int.',Q2y_int.',Q2z_int.',Roll3_int.',Pitch3_int.',Yaw3_int.',Acc3x_int.',Acc3y_int.',Acc3z_int.',Gyro3x_int.',Gyro3y_int.',Gyro3z_int.',Q3w_int.',Q3x_int.',Q3y_int.',Q3z_int.'];
   
   %MATLAB has a built-in function to write the matrix given as input  into a file of the format of CSV.
   csvwrite('InterpolatedData.csv',nectar);
   
   
end

                 
   