%% Quadrotor Simulation with LQR Controller and Kalman Filter State Estimator
%  
%  Created by Alejandro Astudillo,
%  alejandro.astudillo@correounivalle.edu.co
%  Industrial Control Research Group - Universidad del Valle, Colombia

            close all;
            clear;
            clc;
            
            global G Gss_d X0 P_k_0 Ts F;
            
            m = 1.568;
            g = 9.807;
            Ixx = 0.0135;
            Iyy = 0.0124;
            Izz = 0.0336;
            
            G = Quadrotor_model4(m, g, Ixx, Iyy, Izz);
            A = G.a;
            B = G.b;
            C = G.c;
            D = G.d;
            [nmeas, ncont] = size(D);      % number of measured and control signals
            
            X0 = initialConditions();
            
            Ts = 0.01;                     % Sample Time [s]
            
            Gss_d = c2d(G,Ts,'zoh');       % Distrete time system
            
            F = lqrDesign(Gss_d);          % Controller design (LQR)
            
            P_k_0 = eye(12);
            
function G = Quadrotor_model4(m, g, Ixx, Iyy, Izz)

            % Number of states:
            nx = 12;

            % Number of inputs:
            nu = 4;

            % Number of outputs:
            ny = 4;
            
        % Linearized Model

            A = [  0  1  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  g  0  0  0   ;
                   0  0  0  1  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  g  0   ;
                   0  0  0  0  0  1  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  1  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  1  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  0   ;
                   0  0  0  0  0  0  0  0  0  0  0  1   ;
                   0  0  0  0  0  0  0  0  0  0  0  0 ] ;
            
            A = A-(1e-3)*eye(12); %To move the eigenvalues from 0 to -0.001

            B = [  0  0  0  0  0 1/m  0  0     0  0      0    0;
                   0  0  0  0  0  0   0 1/Izz  0  0      0    0;
                   0  0  0  0  0  0   0  0     0  1/Iyy  0    0;
                   0  0  0  0  0  0   0  0     0  0      0    1/Ixx ]';

            %C = eye(ny,nx);
            
            C = [1 0 0 0 0 0 0 0 0 0 0 0;
                 0 0 1 0 0 0 0 0 0 0 0 0;
                 0 0 0 0 1 0 0 0 0 0 0 0;
                 0 0 0 0 0 0 1 0 0 0 0 0];

            DD = zeros(ny,nu);

            states = {'X' 'dX' 'Y' 'dY' 'Z' 'dZ'  'Psi' 'dPsi' 'Theta' 'dTheta' 'Phi' 'dPhi' };
            inputs = {'u+mg' 'tao_psi' 'tao_theta' 'tao_phi'};
            outputs = {'X' 'Y' 'Z' 'Psi'};

            G = ss(A,B,C,DD,'statename',states,'inputname',inputs,'outputname',outputs);
end

function F = lqrDesign(Gss_d)
 %% LQR Controller Design
                    
                    % Limits on states
                    pos_max = 1;
                    att_max = 0.25;
                    dpos_max = 1;
                    datt_max = 1;
                    
                    % Limit on control input
                    motor_max = 255; % 255
                    
                    % Cost weights on states
                    x_wght = 0.5/3;
                    xdot_wght = 0.03/3;
                    y_wght = 0.5/3;
                    ydot_wght = 0.03/3;
                    z_wght = 0.5/3;
                    zdot_wght = 0.03/3;
                    psi_wght = 0.175/3;
                    psidot_wght = 0.03/3; % 0.4/3
                    theta_wght = 0.175/3;
                    thetadot_wght = 0.03/3;
                    phi_wght = 0.175/3;
                    phidot_wght = 0.03/3; %0.4/3
                    
                    weights = [x_wght xdot_wght y_wght ydot_wght z_wght zdot_wght psi_wght psidot_wght theta_wght thetadot_wght phi_wght phidot_wght];
                    maxs = [pos_max dpos_max pos_max dpos_max pos_max dpos_max att_max datt_max att_max datt_max att_max datt_max];
                    
                    rho = 0.35;
                    
                    Q = diag(weights./maxs)/sum(weights);
                    R = rho*diag(1./[motor_max motor_max motor_max motor_max]);
                    %Q = 1*(C'*C);
                    % rho = 0.1;       %rho small --> large control effort, good performance
                    %                  %rho large --> small control effort, poor performance
                    %R = rho*eye(4);

                    %[P, eigenvalues, ~] = care(A,B,Q,R);
                    %F = -R\B'*P;
                    %%% The command lqr does the 'care' command internally
                    F = -dlqr(Gss_d.A,Gss_d.B,Q,R);
end

function X0 = initialConditions()
            x_Init         =  0         ;
            x_dot_Init     =  0           ;
            y_Init         =  0          ;
            y_dot_Init     =  0           ;
            z_Init         =  0           ;
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
end
