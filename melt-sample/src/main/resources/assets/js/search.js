var Stuart = {
    search : function(limit){
        var search_url = "lessThan/";
        limit |= 7;
        search_url += limit;
        $.get(search_url, Stuart.handle_search_response, "JSON")
    },

    handle_search_response : function(customers){
        var template = "<h3 customer_id='${id}'><span class='custom-name'>${name}</span></h3>" +
        "<div><p class='brands-title'>Current brands: </p><p>{{each profiles}}<img class ='brand-img' src='/assets/css/images/brands/${$value.brand}.jpg'>{{/each}}</p>" +
        "<p class='brands-title'>Recommend brands: </p><p id='brands_for_${id}'></p></div>";
        $.template( "customerTemplate", template );

        var customers_container = $("#customers_container");
        customers_container.empty();
        customers_container.html("<div id='accordion'></div>");
        $.each(customers, function(index, customer){
            $.tmpl("customerTemplate", customer).appendTo("#accordion")
        });
        $("#accordion").accordion({
        	heightStyle: "content"
        });

        Stuart.first_customer_recommend_brands(customers);
    },

    first_customer_recommend_brands : function(customers){
        if(customers.length > 0){
            Recommendation.recommend(customers[0].id);
        }
    }
};

var Recommendation = {
    init : function(){
        $("#customers_container").on("click", "h3", function(){
            Recommendation.recommend($(this).attr("customer_id"));
        })
    },

    recommend: function(id){
        var recommend_url = "recommendFor/";
        recommend_url += id;

        $.get(recommend_url, function(brands){
            var images = "";
            $.each(brands, function(index, brand){
                images += "<img class ='brand-img' src='/assets/css/images/brands/" + brand + ".jpg'>"
            });
            $("#brands_for_" + id).html(images)
        }, "JSON")
    }
};

