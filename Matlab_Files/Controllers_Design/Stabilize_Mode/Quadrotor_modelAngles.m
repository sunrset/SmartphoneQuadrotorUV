%% Quad-rotor Model
%   
%  Universidad del Valle - Research Group of Industrial Control
%  Alejandro Astudillo Vigoya, 08.12.2016
%
%  updated: 26.10.2017


%% Quad-rotor Model

function G = Quadrotor_modelAngles()

            % Gravity constant
              g = 9.807;

              m = 1.5680;
              Ixx = 0.0135;
              Iyy = 0.0124;
              Izz = 0.0336;

            % Number of states:
            nx = 6;

            % Number of inputs:
            nu = 4;

            % Number of outputs:
            ny = 3;

            psi_Init       =  0 * pi/180 ;
            psi_dot_Init   =  0           ;
            theta_Init     =  0 * pi/180 ;
            theta_dot_Init =  0           ;
            phi_Init       =  0 * pi/180 ;
            phi_dot_Init   =  0           ;

            X0 = [psi_Init        ;
                  psi_dot_Init    ;
                  theta_Init      ;
                  theta_dot_Init  ;
                  phi_Init        ;
                  phi_dot_Init   ];

        % Nonlinear Model

            %Inputs = U, States
            %Outputs = States_dot  
            %States: Psi dPsi Theta dTheta Phi dPhi
            %        1     2     3     4     5   6
            
            States = X0;
            U = [0,0,0,0];

            dPsi = States(2);
            ddPsi = (((Ixx-Iyy)/Izz)*States(6)*States(4)) + (U(2)/Izz);
            dTheta = States(4);
            ddTheta = (((Izz-Ixx)/Iyy)*States(6)*States(2)) + (U(3)/Iyy);
            dPhi = States(6);
            ddPhi = (((Iyy-Izz)/Ixx)*States(4)*States(2)) + (U(4)/Ixx);

            States_dot = [dPsi, ddPsi, dTheta, ddTheta, dPhi, ddPhi]';

        % Linearized Model

            A = [  0  1  0  0  0  0   ;
                   0  0  0  0  0  0   ;
                   0  0  0  1  0  0   ;
                   0  0  0  0  0  0   ;
                   0  0  0  0  0  1   ;
                   0  0  0  0  0  0 ] ;
            
            A = A-(1e-3)*eye(6); %To move the eigenvalues from 0 to -0.001

            B = [  0  0     0  0      0    0;
                   0 1/Izz  0  0      0    0;
                   0  0     0  1/Iyy  0    0;
                   0  0     0  0      0    1/Ixx ]';

            %C = eye(ny,nx);
            
            C = [1 0 0 0 0 0;
                 0 0 1 0 0 0;
                 0 0 0 0 1 0];

            DD = zeros(ny,nu);

            states = {'Psi' 'dPsi' 'Theta' 'dTheta' 'Phi' 'dPhi' };
            inputs = {'u-mg' 'tao_psi' 'tao_theta' 'tao_phi'};
            outputs = {'Psi' 'Theta' 'Phi'};

            Gss = ss(A,B,C,DD,'statename',states,'inputname',inputs,'outputname',outputs);

            %Gss=ss(A,B,C,DD);
            Gssmin=ss(Gss,'min');
            %G=Gssmin;
            G = Gss;

end