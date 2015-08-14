if [[ $OSTYPE == darwin* ]]; then 

	if test ! $(which brew); then
  	echo "Installing homebrew..."
  	ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"	
  	echo "Installing homebrew cask..."
  	brew install caskroom/cask/brew-cask
	fi

	# Update homebrew recipes
	echo "Updating homebrew recipes..."
	brew update

	if test ! $(which virtualbox); then
	echo "Installing Virtualbox..."
		brew cask install virtualbox
	fi
	
	if test ! $(which docker); then
  		echo 'Installing Docker...' >&2
  		brew install docker
  		brew link docker
	fi
	
	if test ! $(which boot2docker); then
  		echo 'Installing Boot2docker...' >&2
  		brew install boot2docker
  		boot2docker init
	fi
	
	boot2docker down
	boot2docker up
	boot2docker ip
	docker run -d -p 0.0.0.0:9000:9000 noddybear/star
fi

