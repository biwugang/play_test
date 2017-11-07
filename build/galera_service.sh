#!/bin/sh

function galera_service_exist_check(){
    local consul_service=$(docker service ls | awk '/galara/{ print $2}')
    echo "consul service is: ${consul_service}"

    if [ -z ${consul_service} ]
    then
        return 1
    else
        return 0
    fi
}

function galera_restart(){
    local galera_seed=$(docker service create --name galera-seed --network registry-network --replicas 1 --env-file _env --mount type=bind,source=/mnt/registry/mariadb,destination=/var/lib/mysql mariadb:0.4.0 seed  2> /dev/null)
    
    if [ $? -eq 0 ]
    then
        echo "galera seed start success"
    else
        echo "galera seed start fail !!"
        return $?
    fi
    
    local galera=$(docker service create --name galera --network registry-network --replicas 3 --env-file _env mariadb:0.4.0 node tasks.galera-seed,tasks.galera 2> /dev/null)
    
    if [ $? -eq 0 ]
    then
        echo "galera node start success"
    else
        echo "galera node start fail !!"
    fi
    
    return  $?
}

function galera_cluster_check(){
    local galera_service=$(docker service ls | awk '/galera/{ print $2}')
    echo "galera service is: $galera_service"

    local galera_container_id=$(docker service ps --no-trunc $galera_service | grep $galera_service | head -1 | awk '{print $2"."$1}' 2> /dev/null )
    echo "galera container ID is: ${galera_container_id}"

    local galera_status=$(docker exec -it $consul_container_id mysql -u root -pqwer --exec="SHOW STATUS LIKE 'wsrep%';" 2> /dev/null)
    echo "galera status is: ${galera_status}"
}

function consul_deploy(){
    echo "=======> galera cluster deploy "
    
    #check galera service is start or not,
    #if galera service is not start, restart galera service

    galera_service_exist_check

    if [ $? -eq 0 ]
    then
        echo "galera service is already start"
    else
        echo "galera service not start, restart galera service"
        galera_restart
        if [ $? -eq 0 ]
        then
            echo "galera start success"
        else
            echo "galera start fail!!!, exit"
            exit -1
        fi
    fi
}
