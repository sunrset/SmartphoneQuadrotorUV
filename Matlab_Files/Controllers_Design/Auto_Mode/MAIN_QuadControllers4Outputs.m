%% H-infinity Controller Design (with controller order reduction) and LQG Controller design for a Quad-rotor 
%   
%  Universidad del Valle - Research Group of Industrial Control
%  Alejandro Astudillo Vigoya, 08.12.2016
%  email: alejandro.astudillo@correounivalle.edu.co

%% Quad-rotor Model
            close all;
            clear;
            clc;
            
            G = Quadrotor_model4();
            A = G.a;
            B = G.b;
            C = G.c;
            D = G.d;
            [nmeas, ncont] = size(D);      % number of measured and control signals
            
            m = 1.568;
            g = 9.807;
%% Controllability and Observability

            Wc = gram(G,'c');
            Wo = gram(G,'o');
            [~,pc] = chol(Wc);
            [~,po] = chol(Wo);
            
            if pc == 0
                disp('Wc is positive definite. The system is controllable');
            else 
                disp('The system is not controllable');
            end
            if po == 0
                disp('Wo is positive definite. The system is observable');
            else 
                disp('The system is not observable');
            end
            
%% Initial Conditions

        x_Init         =  0           ;
        x_dot_Init     =  0           ;
        y_Init         =  1           ;
        y_dot_Init     =  0           ;
        z_Init         =  2           ;
        z_dot_Init     =  0           ;
        psi_Init       =  0 * pi/180 ;
        psi_dot_Init   =  0           ;
        theta_Init     =  0 * pi/180 ;
        theta_dot_Init =  0           ;
        phi_Init       =  0 * pi/180 ;
        phi_dot_Init   =  0           ;

        X0 = [x_Init          ;
              x_dot_Init      ;
              y_Init          ;
              y_dot_Init      ;
              z_Init          ;
              z_dot_Init      ;
              psi_Init        ;
              psi_dot_Init    ;
              theta_Init      ;
              theta_dot_Init  ;
              phi_Init        ;
              phi_dot_Init   ];
      

        %% H-infinity Controller Design
                %% Performance and Robustness Bounds

                    s = zpk('s'); % Laplace variable s

                    ws = 1e-4; Ms = 1e-4; 
                    %wk = 2e1;  Mk = 2e1;   c = 1e3;
                    %ws = 22e-7; Ms = 1e-4; % simulation working with: ws = 22e-7; Ms = 1e-4; 
                    wk = 3e1;  Mk = 2e1;   c = 1e4; % simulation working with: wk = 5e1;  Mk = 2e1;   c = 1e3;


                    Ws = (ws/Ms)/(s + ws);

                    Wk = 1/Mk * tf([1/wk 1],[1/(c*wk) 1]); 

                    WS = [Ws 0  0  0  ;   
                          0  Ws 0  0  ;   
                          0  0  Ws 0  ;
                          0  0  0  Ws];

                    WK = [Wk 0  0  0  ;   
                          0  Wk 0  0  ;   
                          0  0  Wk 0  ;
                          0  0  0  Wk];
                   

                %% Compute H-infinity optimal controller for LTI plant
                  
                    P=augw(G,WS,WK,[]);         % Construct the generalized plant
                    [K1,CL,hinf]=hinfsyn(P);

                    disp('Gamma = ');
                    disp(hinf);
                    iteraciones = 0;

                    while hinf > 1.0001 || hinf < 0.8
                        P=augw(G,WS/(hinf),WK/(hinf),[]);
                        [K1,CL,hinf]=hinfsyn(P);

                        iteraciones = iteraciones + 1;
                        disp('Gamma = ');
                        disp(hinf);
                        disp('End of Iteration Number: ');
                        disp(iteraciones);
                    end
                    
                    %gammamin = 0.970;
                    %gammamax = 1.000;
                    %gammatol = 0.01;
                    %[K1,CL,hinf]=hinfsyn(P,nmeas,ncont,gammamin,gammamax,gammatol);
                    
                    
                    %% Order Reduction
                    reduction_technique = 1;
                        % 1 --> Balanced truncation
                        % 2 --> Balanced residualization
                        % 3 --> Optimal Hankel norm approximation
                        % 4 --> Balanced model truncation via Schur method
                        
                    switch reduction_technique
                        case 1  % Controller Order Reduction (Balanced Truncation)

                                %[K1br,hsv] = balreal(K1);  % Compute Balanced Realization and Hankel Singular Values
                                %elim = (hsv<1e-5);         % Small entries of hsv are negligible states
                                %K1 = modred(K1br,elim,'Truncate'); % Remove negligible states
                                
                                % This three commands can be reduced to
                                % just the command 'balancmr'

                                [K1, hsvinfo] = balancmr(K1,16);
                                %K1 = balancmr(K1);        %If the desired order isn't included, the HSV will be plot and you will be asked to enter the desired order in the command window
                                
                                % The command 'balancmr' do all about
                                % computing the balanced realization,
                                % showing the negligible states and then
                                % removing the n-r negligible states.

                        case 2  % Controller Order Reduction (Balanced Residualization)

                                [K1br,hsv] = balreal(K1);  % Compute Balanced Realization and Hankel Singular Values
                                elim = (hsv<1e-5);          % Small entries of hsv are negligible states
                                K1 = modred(K1br,elim,'MatchDC'); % Remove negligible states

                        case 3  % Controller Order Reduction (Optimal Hankel norm approximation)

                                K1 = hankelmr(K1,16); %If the desired order isn't included, the HSV will be plot and you will be asked to enter the desired order in the command window

                        case 4  % Balanced model truncation via Schur method

                                K1 = schurmr(K1,16); %If the desired order isn't included, the HSV will be plot and you will be asked to enter the desired order in the command window
                    end
                    
                    
                    %% Discretize the H-inf Controller
                    Ts = 0.01;
                    K1d = c2d(K1,Ts,'zoh');
                    
                    %% Sensitivities Test
                    
                    Gss = ss(A+0.001*eye(12),B,C,D); %The model is taken back to normal.

                    L = Gss*K1;
                    S = inv(eye(4) + L);            %Sensitivity function S = (I + GK)^-1
                    KS = K1/(eye(4) + L);           %Control Sensitivity function KS = K(I + GK)^-1                

                    figure(3)
                    subplot(211);   sigma(S,'-b',inv(WS),'--r'); title('S'); legend('S', '1/|Ws|');
                    subplot(212);   sigma(KS,'-b',inv(WK),'--r'); title('KS'); legend('KS', '1/|Wk|');
                    
        %% LQG Controller Design
                %% LQR Controller Design

                    Q = 1*(C'*C);
                    rho = 0.7;       %rho small --> large control effort, good performance
                                     %rho large --> small control effort, poor performance
                    R = rho*eye(4);

                    [P, eigenvalues, ~] = care(A,B,Q,R);
                    F = -R\B'*P;
                    %F = -lqr(A,B,Q,R); 
                    %%% The command lqr does the 'care' command internally

                %% LQE Observer Design

                    Qe = B*B';
                    Re = 0.9*eye(4);
                    [Pe, eigenvaluese, ~] = care(A',C',Qe,Re);
                    Fe = -Pe*C'/Re;  
         

     %% Simulation 
            controller = 1;
                % 1 --> H-inf controller desing
                % 2 --> LQG controller design
                % 3 --> Both H-inf and LQG controller design
            
             path = 4;
                % 1 --> XY circle path with Z up and down
                % 2 --> X-mas Tree
                % 3 --> Mapping Mission
                % 4 --> Long Mapping Mission
                
            GclH = feedback(series(K1,Gss),eye(4,4));
            %[GclH, hsvinfo] = balancmr(GclH,16);
            
            Gcllqg = ss(A+B*F+Fe*C,B,C,[]);
            v = inv(C*inv(A+B*F+Fe*C)*B);
            Gcllqg = series(-v,Gcllqg);
            
            sample_time = 0.02;
            total_time = 36;
            
            clear waypoints;
            
            switch path
                case 1 %XY circle path with Z up and down
                    %waypont# must be a vector like: [X Y Z Yaw 0 0]';
                    waypoints(:,1) = [3 0 0.25 15*pi/180 0 0]';
                    waypoints(:,2) = [2.9 2.9 0.5 30*pi/180 0 0]';
                    waypoints(:,3) = [0.2 2.8 0.75 15*pi/180 0 0]';
                    waypoints(:,4) = [0.3 0.3 1 0*pi/180 0 0]';
                    waypoints(:,5) = [2.6 0.4 0.75 15*pi/180 0 0]';
                    waypoints(:,6) = [2.5 2.5 0.5 30*pi/180 0 0]';
                    waypoints(:,7) = [0.6 2.4 0.25 15*pi/180 0 0]';
                    waypoints(:,8) = [0.7 0.7 0 0*pi/180 0 0]';
                    waypoints(:,9) = [2.2 0.8 0.25 15*pi/180 0 0]';
                    waypoints(:,10) = [2.1 2.1 0.5 30*pi/180 0 0]';
                    waypoints(:,11) = [0.9 2.0 0.75 15*pi/180 0 0]';
                    waypoints(:,12) = [1.0 1.0 1 0*pi/180 0 0]';
                    
                case 2 %X-mas Tree
                    %waypont# must be a vector like: [X Y Z Yaw 0 0]';
                    waypoints(:,1) = [3 0 0.25 15*pi/180 0 0]';
                    waypoints(:,2) = [2.9 2.9 0.5 30*pi/180 0 0]';
                    waypoints(:,3) = [0.2 2.8 0.75 15*pi/180 0 0]';
                    waypoints(:,4) = [0.3 0.3 1 0*pi/180 0 0]';
                    waypoints(:,5) = [2.6 0.4 1.25 15*pi/180 0 0]';
                    waypoints(:,6) = [2.5 2.5 1.5 30*pi/180 0 0]';
                    waypoints(:,7) = [0.6 2.4 1.75 15*pi/180 0 0]';
                    waypoints(:,8) = [0.7 0.7 2.0 0*pi/180 0 0]';
                    waypoints(:,9) = [2.2 0.8 2.25 15*pi/180 0 0]';
                    waypoints(:,10) = [2.1 2.1 2.5 30*pi/180 0 0]';
                    waypoints(:,11) = [0.9 2.0 2.75 15*pi/180 0 0]';
                    waypoints(:,12) = [1.0 1.0 3 0*pi/180 0 0]';
                    
                case 3 %Mapping Mission
                    %waypont# must be a vector like: [X Y Z Yaw 0 0]';
                    waypoints(:,1) = [2 0 0.5 15*pi/180 0 0]';
                    waypoints(:,2) = [4 0 1 30*pi/180 0 0]';
                    waypoints(:,3) = [4 4 1 15*pi/180 0 0]';
                    waypoints(:,4) = [2 4 1 0*pi/180 0 0]';
                    waypoints(:,5) = [0 4 1 15*pi/180 0 0]';
                    waypoints(:,6) = [0 8 1 30*pi/180 0 0]';
                    waypoints(:,7) = [2 8 1 15*pi/180 0 0]';
                    waypoints(:,8) = [4 8 1 0*pi/180 0 0]';
                    waypoints(:,9) = [4 12 1 15*pi/180 0 0]';
                    waypoints(:,10) = [2 12 1 30*pi/180 0 0]';
                    waypoints(:,11) = [0 12 1 15*pi/180 0 0]';
                    waypoints(:,12) = [0 16 1 0*pi/180 0 0]';

                case 4 %Long Mapping Mission
                    %waypont# must be a vector like: [X Y Z Yaw 0 0]';
                    waypoints(:,1) = [2 0 5 0*pi/180 0 0]';
                    waypoints(:,2) = [4 0 10 0*pi/180 0 0]';
                    waypoints(:,3) = [4 4 10 0*pi/180 0 0]';
                    waypoints(:,4) = [2 4 10 0*pi/180 0 0]';
                    waypoints(:,5) = [0 4 10 0*pi/180 0 0]';
                    waypoints(:,6) = [0 8 10 0*pi/180 0 0]';
                    waypoints(:,7) = [2 8 10 0*pi/180 0 0]';
                    waypoints(:,8) = [4 8 10 10*pi/180 0 0]';
                    waypoints(:,9) = [4 12 10 10*pi/180 0 0]';
                    waypoints(:,10) = [2 12 10 10*pi/180 0 0]';
                    waypoints(:,11) = [0 12 10 10*pi/180 0 0]';
                    waypoints(:,12) = [0 16 10 10*pi/180 0 0]';
                    waypoints(:,13) = [2 16 10 10*pi/180 0 0]';
                    waypoints(:,14) = [4 16 10 0*pi/180 0 0]';
                    waypoints(:,15) = [4 20 10 0*pi/180 0 0]';
                    waypoints(:,16) = [3 20 6.66 0*pi/180 0 0]';
                    waypoints(:,17) = [1.5 20 3.33 0*pi/180 0 0]';
                    waypoints(:,18) = [0 20 0 0*pi/180 0 0]';
            
            end
            
            [desired_Traj, t2] = GenerateSPLINETrajectory(waypoints,sample_time,total_time); %Generate trajectories
            
            switch controller
                case 1 %Simulate the response of the H-infinity Controller
                    [y,t,x] = lsim(GclH,desired_Traj(1:4,:),t2,zeros(size(GclH.A,1),1));
                    
                    figure(1);
                    plot(t,y(:,1),'-r'), hold on;
                    plot(t,y(:,2),'-g'),
                    plot(t,y(:,3),'-b'),
                    legend('X', 'Y', 'Z');
                    title('Position response');

                    figure(10)
                    plot3(desired_Traj(1,:)',desired_Traj(2,:)',desired_Traj(3,:)','-r',waypoints(1,:),waypoints(2,:),waypoints(3,:),'*y',y(:,1),y(:,2),y(:,3),'-g','LineWidth',2);
                    grid on;
                    legend('Desired Trajectory', 'Waypoints', 'Actual Trajectory');
                    title('XYZ Position of the Quadrotor');
                    xlabel('X (m)','FontSize',10);
                    ylabel('Y (m)','FontSize',10);
                    zlabel('Z (m)','FontSize',10);
   
                    
                    figure(11)
                    subplot(2,2,1);
                    plot(t,y(:,1),'g',t,desired_Traj(1,:)','r');
                    legend('Actual', 'Desired');
                    title('X position comparative');
                    subplot(2,2,2);
                    plot(t,y(:,2),'g',t,desired_Traj(2,:)','r');
                    legend('Actual', 'Desired');
                    title('Y position comparative');
                    subplot(2,2,3);
                    plot(t,y(:,3),'g',t,desired_Traj(3,:)','r');
                    legend('Actual', 'Desired');
                    title('Z position comparative');
                    subplot(2,2,4);
                    plot(t,(180/pi).*y(:,4),'g',t,(180/pi).*desired_Traj(4,:)','r');
                    legend('Actual', 'Desired');
                    title('Yaw comparative');
                    
                case 2  %Simulate the response of the LQG Controller
                    [y,t,x] = lsim(Gcllqg,desired_Traj(1:4,:),t2,zeros(size(Gcllqg.A,1),1));
                    
                    figure(1);
                    plot(t,y(:,1),'-r'), hold on;
                    plot(t,y(:,2),'-g'),
                    plot(t,y(:,3),'-b'),
                    legend('X', 'Y', 'Z');
                    title('Position response');


                    figure(10)
                    plot3(desired_Traj(1,:)',desired_Traj(2,:)',desired_Traj(3,:)','-r',waypoints(1,:),waypoints(2,:),waypoints(3,:),'*y',y(:,1),y(:,2),y(:,3),'-g','LineWidth',2);
                    grid on;
                    legend('Desired Trajectory', 'Waypoints', 'Actual Trajectory');
                    title('XYZ Position of the Quadrotor');
                    xlabel('X (m)','FontSize',10);
                    ylabel('Y (m)','FontSize',10);
                    zlabel('Z (m)','FontSize',10);

                    figure(11)
                    subplot(2,2,1);
                    plot(t,y(:,1),'g',t,desired_Traj(1,:)','r');
                    legend('Actual', 'Desired');
                    title('X position comparative');
                    subplot(2,2,2);
                    plot(t,y(:,2),'g',t,desired_Traj(2,:)','r');
                    legend('Actual', 'Desired');
                    title('Y position comparative');
                    subplot(2,2,3);
                    plot(t,y(:,3),'g',t,desired_Traj(3,:)','r');
                    legend('Actual', 'Desired');
                    title('Z position comparative');
                    subplot(2,2,4);
                    plot(t,(180/pi).*y(:,4),'g',t,(180/pi).*desired_Traj(4,:)','r');
                    legend('Actual', 'Desired');
                    title('Yaw comparative');

                case 3  %Simulate the response of both the H-infinity and the LQG Controller
                    [y,t,x] = lsim(GclH,desired_Traj(1:4,:),t2,zeros(size(GclH.A,1),1));
                    [yl,tl,xl] = lsim(Gcllqg,desired_Traj(1:4,:),t2,zeros(size(Gcllqg.A,1),1));
                    
                    figure(1);
                    plot(t,y(:,1),'-r'), hold on;
                    plot(t,y(:,2),'-g'),
                    plot(t,y(:,3),'-b'),
                    plot(t,yl(:,1),'-.r'),
                    plot(t,yl(:,2),'-.g'),
                    plot(t,yl(:,3),'-.b'),
                    legend('X H-inf', 'Y H-inf', 'Z H-inf','X LQR', 'Y LQR', 'Z LQR');
                    title('Position response (Both Controllers)');

                    figure(10)
                    plot3(yl(:,1),yl(:,2),yl(:,3),'-b',waypoints(1,:),waypoints(2,:),waypoints(3,:),'*y',y(:,1),y(:,2),y(:,3),'-g','LineWidth',2);
                    grid on;
                    legend('LQR Trajectory', 'Waypoints', 'H-inf Trajectory');
                    title('XYZ Position of the Quadrotor');
                    xlabel('X (m)','FontSize',10);
                    ylabel('Y (m)','FontSize',10);
                    zlabel('Z (m)','FontSize',10);

                    figure(11)
                    subplot(2,2,1);
                    plot(t,y(:,1),'g',t,desired_Traj(1,:)','r');
                    legend('Actual', 'Desired');
                    title('X position comparative');
                    subplot(2,2,2);
                    plot(t,y(:,2),'g',t,desired_Traj(2,:)','r');
                    legend('Actual', 'Desired');
                    title('Y position comparative');
                    subplot(2,2,3);
                    plot(t,y(:,3),'g',t,desired_Traj(3,:)','r');
                    legend('Actual', 'Desired');
                    title('Z position comparative');
                    subplot(2,2,4);
                    plot(t,(180/pi).*y(:,4),'g',t,(180/pi).*desired_Traj(4,:)','r');
                    legend('Actual', 'Desired');
                    title('Yaw comparative');
                    
                    errorH = sqrt((y(:,1)-desired_Traj(1,:)').^2 + (y(:,2)-desired_Traj(2,:)').^2 + (y(:,3)-desired_Traj(3,:)').^2);
                    errorLQG = sqrt((yl(:,1)-desired_Traj(1,:)').^2 + (yl(:,2)-desired_Traj(2,:)').^2 + (yl(:,3)-desired_Traj(3,:)').^2);
                    figure(71)
                    plot(t(1:1900),errorH(1:1900),t(1:1900),errorLQG(1:1900),'r');
                    legend('Position Error with H\infty Controller','Position Error with LQG Controller');
                    grid on;
                    %title('Position Error');
                    xlabel('Time (s)','FontSize',10);
                    ylabel('Absolute Error (m)','FontSize',10);
            
               
            end
            
            