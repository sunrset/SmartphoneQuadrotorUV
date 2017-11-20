%% H-infinity Controller Design (with controller order reduction) and LQG Controller design for a Quad-rotor 
%   
%  Universidad del Valle - Research Group of Industrial Control
%  Alejandro Astudillo Vigoya, 08.12.2016
%  email: alejandro.astudillo@correounivalle.edu.co

%% Quad-rotor Model
            close all;
            clear all;
            clc;
            
            G = Quadrotor_modelAltHold();
            A = G.a;
            B = G.b;
            C = G.c;
            D = G.d;
            [nmeas, ncont] = size(D);      % number of measured and control signals
            
           
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

        z_Init         =  2           ;
        z_dot_Init     =  0           ;
        psi_Init       =  0 * pi/180 ;
        psi_dot_Init   =  0           ;
        theta_Init     =  0 * pi/180 ;
        theta_dot_Init =  0           ;
        phi_Init       =  0 * pi/180 ;
        phi_dot_Init   =  0           ;

        X0 = [z_Init          ;
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
                    wk = 2e1;  Mk = 2e1;   c = 1e3;

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

                    while hinf > 1.0000 || hinf < 0.8
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

                                [K1, hsvinfo] = balancmr(K1,10);
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

                                K1 = hankelmr(K1,10); %If the desired order isn't included, the HSV will be plot and you will be asked to enter the desired order in the command window

                        case 4  % Balanced model truncation via Schur method

                                K1 = schurmr(K1,10); %If the desired order isn't included, the HSV will be plot and you will be asked to enter the desired order in the command window
                    end
                    
                    
                    %% Discretize the H-inf Controller
                    Ts = 0.01;
                    K1d = c2d(K1,Ts,'zoh');
                    
                    %% Sensitivities Test
                    
                    Gss = ss(A+0.001*eye(8),B,C,D); %The model is taken back to normal.
                    
                    L = Gss*K1;
                    S = inv(eye(4) + L);            %Sensitivity function S = (I + GK)^-1
                    KS = K1/(eye(4) + L);           %Control Sensitivity function KS = K(I + GK)^-1                

                    figure(3)
                    subplot(211);   sigma(S,'-b',inv(WS),'--r'); title('S'); legend('S', '1/|Ws|');
                    subplot(212);   sigma(KS,'-b',inv(WK),'--r'); title('KS'); legend('KS', '1/|Wk|');
                    
        %% LQG Controller Design
                %% LQR Controller Design
                
                    % Limits on states
                    pos_max = 0.25;
                    att_max = 0.25;
                    dpos_max = 1;
                    datt_max = 1;
                    
                    % Limit on control input
                    motor_max = 255; % 255
                    
                    % Cost weights on states
                    z_wght = 0.001/3;
                    zdot_wght = 0.03/3;
                    psi_wght = 0.175/3;
                    psidot_wght = 0.03/3; % 0.4/3
                    theta_wght = 0.175/3;
                    thetadot_wght = 0.03/3;
                    phi_wght = 0.175/3;
                    phidot_wght = 0.03/3; %0.4/3
                    
                    weights = [z_wght zdot_wght psi_wght psidot_wght theta_wght thetadot_wght phi_wght phidot_wght];
                    maxs = [pos_max dpos_max att_max datt_max att_max datt_max att_max datt_max];
                    
                    rho = 0.4;
                    
                    Q = diag(weights./maxs)/sum(weights);
                    R = rho*diag(1./[motor_max motor_max motor_max motor_max]);

                    %Q = 1*(C'*C);
                    %rho = 0.7;       %rho small --> large control effort, good performance
                                     %rho large --> small control effort, poor performance
                    %R = rho*eye(4);

                    [P, eigenvalues, ~] = care(A,B,Q,R);
                    %F = -R\B'*P;
                    %F = -lqr(A,B,Q,R); 
                    %%% The command lqr does the 'care' command internally
                    
                    Gss_d = c2d(Gss,Ts,'zoh');
                    F = -dlqr(Gss_d.A,Gss_d.B,Q,R);
                    
                    F(1,1) = -1.5680;
                    F(1,2) = -0.6301;
                    F(2,3) = -1.7045;
                    F(2,4) = -0.21785;
                    F(3,5) = -1.1448;
                    F(3,6) = -0.3107;
                    F(4,7) = -1.2461;
                    F(4,8) = -0.3381;
                   
                    F
                %% LQE Observer Design

                    %Qe = B*B';
                    %Re = 0.9*eye(4);
                    %[Pe, eigenvaluese, ~] = care(A',C',Qe,Re);
                    %Fe = -Pe*C'/Re;  
                    %Qn = eye(4);
                    %[kest,Lk,Pk,Mk,Zk] = kalmd(Gss,Qn,Re,Ts);
                    %kest = estim(Gss,-Fe,[1,2,3,4],[1,2,3,4]);
                    Qe = Gss_d.B*Gss_d.B';
                    Re = 0.9*eye(4);
                    [Pe, eigenvaluese, ~] = care(Gss_d.A',Gss_d.C',Qe,Re);
                    Fe = -Pe*C'/Re;  

     %% Simulation 
            controller = 2;
                % 1 --> H-inf controller desing
                % 2 --> LQG controller design
                % 3 --> Both H-inf and LQG controller design
            
                
            GclH = feedback(series(K1,Gss),eye(4,4));
            %[GclH, hsvinfo] = balancmr(GclH,16);
            
            %Gcllqg = ss(A+B*F,B*1.1827,C,[]);
            Gcllqg = ss(A+B*F+Fe*C,B,C,[]);

            v = inv(C*inv(A+B*F+Fe*C)*B);
            Gcllqg = series(-v,Gcllqg);
            
            sample_time = Ts;
            total_time = 75;
            
            clear waypoints;
            
                    %waypont# must be a vector like: [Yaw Roll Pitch]';
                    waypoints(:,1) = ([2 [0 10 0].*pi/180])';
                    waypoints(:,2) = ([2 [5 15 0].*pi/180])';
                    waypoints(:,3) = ([2 [10 20 0].*pi/180])';
                    waypoints(:,4) = ([2 [5 15 -5].*pi/180])';
                    waypoints(:,5) = ([2 [0 10 -10].*pi/180])';
                    waypoints(:,6) = ([2 [-5 5 -5].*pi/180])';
                    waypoints(:,7) = ([2 [-3 0 0].*pi/180])';
                    waypoints(:,8) = ([2 [-1 0 0].*pi/180])';
                    waypoints(:,9) = ([2 [0 0 0].*pi/180])';
                    waypoints(:,10) = ([2 [0 0 0].*pi/180])';
                    waypoints(:,11) = ([2 [0 0 0].*pi/180])';
                    waypoints(:,12) = ([2 [0 0 0].*pi/180])';
            
            [desired_Traj, t2] = GenerateSPLINETrajectory(waypoints,sample_time,total_time); %Generate trajectories
            switch controller
                case 1 %Simulate the response of the H-infinity Controller
                    [y,t,x] = lsim(GclH,desired_Traj(1:4,:),t2,zeros(size(GclH.A,1),1));
                    
                    figure(1);
                    plot(t,y(:,1),'-c'), hold on;
                    plot(t,y(:,2),'-r'),
                    plot(t,y(:,3),'-g'),
                    plot(t,y(:,4),'-b'),
                    legend('Z', 'Yaw', 'Roll', 'Pitch');
                    title('Quadrotor response');

                    figure(11)
                    subplot(2,2,1);
                    plot(t,y(:,1),'g',t,desired_Traj(1,:)','r');
                    legend('Actual', 'Desired');
                    title('Z comparative');
                    subplot(2,2,2);
                    plot(t,(180/pi).*y(:,2),'g',t,(180/pi).*desired_Traj(2,:)','r');
                    legend('Actual', 'Desired');
                    title('Yaw comparative');
                    subplot(2,2,3);
                    plot(t,(180/pi).*y(:,3),'g',t,(180/pi).*desired_Traj(3,:)','r');
                    legend('Actual', 'Desired');
                    title('Roll comparative');
                    subplot(2,2,4);
                    plot(t,(180/pi).*y(:,4),'g',t,(180/pi).*desired_Traj(4,:)','r');
                    legend('Actual', 'Desired');
                    title('Pitch comparative');
               case 2  %Simulate the response of the LQG Controller
                    %desired_Traj = [ zeros(1,size(desired_Traj,2)); desired_Traj];
                    [y,t,x] = lsim(Gcllqg,desired_Traj(1:4,:),t2,zeros(size(Gcllqg.A,1),1));
                    
                    figure(1);
                    plot(t,y(:,1),'-c'), hold on;
                    plot(t,y(:,2),'-r'),
                    plot(t,y(:,3),'-g'),
                    plot(t,y(:,4),'-b'),
                    legend('Z', 'Yaw', 'Roll', 'Pitch');
                    title('Quadrotor response');

                    figure(11)
                    subplot(2,2,1);
                    plot(t,y(:,1),'g',t,desired_Traj(1,:)','r');
                    legend('Actual', 'Desired');
                    title('Z comparative');
                    subplot(2,2,2);
                    plot(t,(180/pi).*y(:,2),'g',t,(180/pi).*desired_Traj(2,:)','r');
                    legend('Actual', 'Desired');
                    title('Yaw comparative');
                    subplot(2,2,3);
                    plot(t,(180/pi).*y(:,3),'g',t,(180/pi).*desired_Traj(3,:)','r');
                    legend('Actual', 'Desired');
                    title('Roll comparative');
                    subplot(2,2,4);
                    plot(t,(180/pi).*y(:,4),'g',t,(180/pi).*desired_Traj(4,:)','r');
                    legend('Actual', 'Desired');
                    title('Pitch comparative');
            end;