clc;
clear;
format compact;
if true
   clear variables
   %% A variable to hold all the data to be read
   data = csvread('InterpolatedData.csv');
   %% read all the data from file
   TimeSensor_pix= data(:,1);
   Roll1_pix = data(:,2);
   Pitch1_pix = data(:,3);
   Yaw1_pix = data(:,4);
   Acc1x_pix = data(:,5);
   Acc1y_pix = data(:,6);
   Acc1z_pix = data(:,7);
   Gyro1x_pix = data(:,8);
   Gyro1y_pix = data(:,9);
   Gyro1z_pix = data(:,10);
   Q1w_pix = data(:,11);
   Q1x_pix = data(:,12);
   Q1y_pix = data(:,13);
   Q1z_pix = data(:,14);
   Roll2_pix = data(:,15);
   Pitch2_pix = data(:,16);
   Yaw2_pix = data(:,17);
   Acc2x_pix = data(:,18);
   Acc2y_pix = data(:,19);
   Acc2z_pix = data(:,20);
   Gyro2x_pix = data(:,21);
   Gyro2y_pix = data(:,22);
   Gyro2z_pix = data(:,23);
   Q2w_pix = data(:,24);
   Q2x_pix = data(:,25);
   Q2y_pix = data(:,26);
   Q2z_pix = data(:,27);
   Roll3_pix = data(:,28);
   Pitch3_pix = data(:,29);
   Yaw3_pix = data(:,30);
   Acc3x_pix = data(:,31);
   Acc3y_pix = data(:,32);
   Acc3z_pix = data(:,33);
   Gyro3x_pix = data(:,34);
   Gyro3y_pix = data(:,35);
   Gyro3z_pix = data(:,36);
   Q3w_pix = data(:,37);
   Q3x_pix = data(:,38);
   Q3y_pix = data(:,39);
   Q3z_pix = data(:,40);
   TotalAccel1 = zeros(1,100);
   TotalAccel2 = zeros(1,100);
   TotalAccel3 = zeros(1,100);
   TotalGyro1 = zeros(1,100);
   TotalGyro2 = zeros(1,100);
   TotalGyro3 = zeros(1,100);
   Sample_rate1 = 66.6303;
   Q = size(Acc1x_pix,1);
   [a,b] = size(Acc1x_pix);
   windowAccelChest = zeros(1,100);
   windowAccelLeftH = zeros(1,100);
   %Find the aggregating average of the Sensor linear acceleration and
   %angular acceleration
   for k=1:a
       TotalAccel1(k)=sqrt((Acc1x_pix(k)^2+ Acc1y_pix(k)^2+ Acc1z_pix(k)^2));
  
       TotalAccel2(k)=sqrt((Acc2x_pix(k)^2+ Acc2y_pix(k)^2+ Acc2z_pix(k)^2));
   
       TotalAccel3(k)=sqrt((Acc3x_pix(k)^2+ Acc3y_pix(k)^2+ Acc3z_pix(k)^2));
       TotalGyro1(k)=sqrt((Gyro1x_pix(k)^2+ Gyro1y_pix(k)^2+ Gyro1z_pix(k)^2));
  
       TotalGyro2(k)=sqrt((Gyro2x_pix(k)^2+ Gyro2y_pix(k)^2+ Gyro2z_pix(k)^2));
   
       TotalGyro3(k)=sqrt((Gyro3x_pix(k)^2+ Gyro3y_pix(k)^2+ Gyro3z_pix(k)^2));
   end
   T = 128;
   %% The mean of the total acceleration and angular velocity commonents
   % will be given below
   modData1 = movmean(TotalAccel1, T);
   modData2 = movmean(TotalAccel2, T);
   modData3 = movmean(TotalAccel3, T);
   modData4 = movmean(TotalGyro1,  T);
   modData5 = movmean(TotalGyro2,  T);
   modData6 = movmean(TotalGyro3,  T);
   
   %% The power spectral density for each components will be calculated below
   [pax, fraq] = pwelch(modData1,[],[],[],67);
   [pex, freq] = pwelch(modData2,[],[],[],67);
   [pix, friq] = pwelch(modData3,[],[],[],67);
   [pox, froq] = pwelch(modData4,[],[],[],67);
   [pux, fruq] = pwelch(modData5,[],[],[],67);
   [puxy, fruqy] = pwelch(modData6,[],[],[],67);
   
   [pack, lacs] = findpeaks(pax);
   [peck, lecs] = findpeaks(pex);
   [pick, lics] = findpeaks(pix);
   [pock, locs] = findpeaks(pox);
   [puck, lucs] = findpeaks(pux);
   [pucky, lucsy] = findpeaks(puxy);
   
   %% The plots of the different components
   plot(fraq(lacs),db(pack),'DisplayName','Chest Sensor');
   hold on
   plot(freq(lecs),db(peck),'g--','DisplayName','Left-Hand Sensor');
   hold on
   plot(friq(lics),db(pick),'r--','DisplayName','Right-Hand Sensor');
   legend('show')
   t = xlabel({'Frequency','(Hz)'});
   t.Color = 'blue';
   ball = ylabel({'Power','(db)'});
   ball.Color = 'red';
   title('PSD of the mean acceleration of an individual Cycling Bar (Forearm Upper-Limb Placement) ');

%    plot(froq(locs),db(pock),'DisplayName','Chest Sensor');
%    hold on
%    plot(fruq(lucs),db(puck),'g--','DisplayName','Left-Hand Sensor');
%    hold on
%    plot(fruqy(lucsy),db(pucky),'r--','DisplayName','Right-Hand Sensor');
%    legend('show')
%    t = xlabel({'Frequency','(Hz)'});
%    t.Color = 'blue';
%    ball = ylabel({'Power','(db)'});
%    ball.Color = 'red';
%    title('PSD of the mean angular velocity of an individual Running Free (Forearm Upper-Limb Placement) ');

nectar=[modData1.',modData2.',modData3.'];
csvwrite('Gringo.csv',nectar);
  
end