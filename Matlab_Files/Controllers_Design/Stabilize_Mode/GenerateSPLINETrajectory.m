function [desired_Traj, t] = GenerateSPLINETrajectory(waypoints,sample_time,total_time)
%GenerateSimpleTrajectory function
%   Inputs:
%   - waypoints
%   - sample_time 
%   - total_time

    initial_conditions = [0 0 0 ]';
    %waypoints = [initial_conditions waypoints waypoints(:,size(waypoints,2)) waypoints(:,size(waypoints,2)) waypoints(:,size(waypoints,2))];
    waypoints = [initial_conditions waypoints];
   
    t = 0:sample_time:total_time;
    
    Xwaypoints = waypoints(1,:);
    Ywaypoints = waypoints(2,:);
    Zwaypoints = waypoints(3,:);  
    
    finalpoint = repmat(waypoints(:,size(waypoints,2)),1,1000);

    t_sampfull = round(total_time/(size(Xwaypoints,2)-1));
    t_full = 0:t_sampfull:total_time;

    spX = spline(t_full,Xwaypoints,t);
    spY = spline(t_full,Ywaypoints,t);
    spZ = spline(t_full,Zwaypoints,t);
    
    desired_Traj = [spX; spY; spZ];
    desired_Traj = [desired_Traj finalpoint];
    t = 0:sample_time:(total_time+round(1000*sample_time));
    
end

