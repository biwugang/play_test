#!/bin/sh

function consul_service_exist_check(){
    local consul_service=$(docker service ls | awk '/consul/{ print $2}')
    echo "consul service is: ${consul_service}"

    if [ -z ${consul_service} ]
    then
        return 1
    else
        return 0
    fi
}

function consul_restart(){
    local consul_service=$(docker service create --name consul-master --network registry-network --replicas 3 progrium/consul -server -bootstrap-expect 3 -retry-join=consul-master -retry-join=consul-master -retry-join=consul-master 2> /dev/null)
    
    if [ $? -eq 0 ]
    then
        echo "consul service start success"
    else
        echo "consul service start fail !!"
    fi
    
    return  $?
}

function consul_cluster_check(){
    local consul_service=$(docker service ls | awk '/consul/{ print $2}')
    echo "consul service is: $consul_service"

    local consul_container_id=$(docker service ps --no-trunc $consul_service | grep $consul_service | head -1 | awk '{print $2"."$1}' 2> /dev/null )
    echo "consul container ID is: ${consul_container_id}"

    local consul_leader=$(docker exec -it $consul_container_id curl http://127.0.0.1:8500/v1/status/leader | awk -F'"' '{print $2}' | awk '/[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}.*/;{print $2}' 2> /dev/null)
    echo "consul leader is: ${consul_leader}"

    if  [ -z ${consul_leader} ]
    then
        return 1
    else
        return 0
    fi
}

function consul_deploy(){
    echo "=======> consul cluster deploy "
    
    #check consul service is start or not,
    #if consul service is not start, restart consul service

    consul_service_exist_check

    if [ $? -eq 0 ]
    then
        echo "consul service is already start"
    else
        echo "consul service not start, restart consul service"
        consul_restart
        if [ $? -eq 0 ]
        then
            echo "consul start success"
        else
            echo "consul start fail!!!, exit"
            exit -1
        fi
    fi


    #check consul cluster status
    while true
    do
        sleep 3
    
        consul_cluster_check
    
        if [ $? -eq 0 ]
        then
           echo "consul cluster is ready"
           break
        else
           echo "consul cluster is not ready, waiting..."
        fi
    done
}
